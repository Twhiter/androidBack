package com.springtest.demo.controller;


import com.springtest.demo.businessEntity.PaySemaphore;
import com.springtest.demo.businessEntity.PaySemaphorePool;
import com.springtest.demo.dto.*;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.Pay;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.redisEntity.SessionPay;
import com.springtest.demo.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@RestController
public class PayController {


    @Autowired
    private PayService payService;

    @Autowired
    private SessionPayService sessionPayService;

    @Autowired
    private PayVerifyService payVerifyService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private RefundService refundService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/pay")
    public ResponseData<PayResp> pay(@ApiIgnore @RequestAttribute("userId") int userId,
                                     @RequestParam(name = "merchantId") int merchantId,
                                     @RequestParam(name = "amount") BigDecimal amount,
                                     @RequestParam(name = "paymentPassword") String paymentPassword,
                                     @RequestParam(name = "remarks")String remarks
                                     ) {

        amount = amount.setScale(4, RoundingMode.HALF_UP);
        ResponseData<PayResp> responseData = new ResponseData<>();
        responseData.data = new PayResp();


        try{
            var promptAndPay = payService.pay(userId, merchantId, amount, paymentPassword, remarks);
            responseData.data.prompt = (Prompt) promptAndPay[0];
            responseData.data.payOverview = PayOverview.fromPay((Pay) promptAndPay[1]);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.errorPrompt = "Error";
            responseData.status = ResponseData.ERROR;
        }
        return responseData;
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/payWithConfirm")
    public ResponseData<PayResp> payWithConfirm(@ApiIgnore @RequestAttribute("userId") int userId,
                                                @RequestParam(name = "sessionId") int sessionId,
                                                @RequestParam(name = "paymentPassword") String paymentPassword,
                                                @RequestParam(name = "remarks") String remarks) {


        ResponseData<PayResp> responseData = new ResponseData<>();
        responseData.data = new PayResp();

        try {

            //if the it is not initialized
            PaySemaphore paySemaphore = PaySemaphorePool.getInstance().get(sessionId);
            if (paySemaphore == null) {
                responseData.data.prompt = Prompt.pay_session_id_error;
                return responseData;
            }

            //only one mobile phone can scan at one time
            try {
                var isOkay = paySemaphore.notScanned.tryAcquire(10, TimeUnit.SECONDS);
                if (!isOkay)
                    throw new InterruptedException();
            } catch (InterruptedException e) {
                e.printStackTrace();
                responseData.data.prompt = Prompt.multiple_user_pay_error;
                return responseData;
            }


            //modify the sessionPay state,indicating prepaid
            Prompt prompt = sessionPayService.phoneScan(sessionId, userId, remarks, paymentPassword);
            if (prompt != Prompt.success) {
                paySemaphore.notScanned.release();
                responseData.data.prompt = prompt;
                return responseData;
            }


            //notify other thread that waiting for user payment
            paySemaphore.isPaid.release();


            //check whether payment  is finished
            try {
                var isOkay = paySemaphore.isFinished.tryAcquire(1, TimeUnit.MINUTES);
                if (!isOkay)
                    throw new InterruptedException();
            } catch (InterruptedException e) {
                e.printStackTrace();

                //reset back
                paySemaphore.isPaid.acquire();

                //reset  scan
                paySemaphore.notScanned.release();
                responseData.data.prompt = Prompt.pay_time_out;
                return responseData;
            }


            //finished okay.
            paySemaphore.isFinished.release();

            //reset back
            paySemaphore.isPaid.acquire();

            //reset phone scan
            paySemaphore.notScanned.release();

            responseData.data = paySemaphore.paySynData.payResp;

            return responseData;

        } catch (Exception e) {
            e.printStackTrace();
            responseData.data.prompt = Prompt.unknownError;
            return responseData;
        }
    }


    @PostMapping("/api/payVerify")
    public ResponseData<VerifyPayResp> verifyPay(@RequestBody String RSAEncryptedBase64String) {


        ResponseData<VerifyPayResp> resp = new ResponseData<>();
        resp.data = new VerifyPayResp();
        resp.data.prompt = Prompt.unknownError;

        try {

            //extract information
            MerchantVerifyInfo verifyInfo = payVerifyService.extractInfo(RSAEncryptedBase64String);
            if (verifyInfo == null) {
                resp.data.prompt = Prompt.pay_verify_request_format_error;
                return resp;
            }

            //fetch sessionPay and check whether it exists or not
            SessionPay sessionPay = sessionPayService.getById(verifyInfo.sessionId);
            if (sessionPay == null) {
                resp.data.prompt = Prompt.pay_time_out;
                return resp;
            }

            // get the paySemaphore and check whether it exists or not
            PaySemaphore paySemaphore = PaySemaphorePool.getInstance().get(sessionPay.sessionId);
            if (paySemaphore == null || paySemaphore.notScanned.availablePermits() == 1) {
                resp.data.prompt = Prompt.pay_time_out;
                return resp;
            }

            //verify the signature
            Prompt prompt = payVerifyService.verify(verifyInfo, sessionPay);
            if (prompt != Prompt.success) {
                resp.data.prompt = prompt;
                return resp;
            }


            //start persistent payment into database
            Object[] promptAndPay = payService.payWithConfirm(sessionPay);
            prompt = (Prompt) promptAndPay[0];
            Pay pay = (Pay) promptAndPay[1];

            if (prompt != Prompt.success) {
                resp.data.prompt = prompt;
                return resp;
            }

            PayResp payResp = new PayResp();
            payResp.prompt = prompt;
            payResp.payOverview = PayOverview.fromPay(pay);

            //remove data in redis
            sessionPayService.delete(sessionPay.sessionId);

            //assign the payment result to paySyn data
            paySemaphore.paySynData.payResp = payResp;

            //release the semaphore in order to inform phoneScan
            paySemaphore.isFinished.release();

            resp.data.prompt = prompt;
            resp.data.payId = pay.payId;
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.data.prompt = Prompt.unknownError;
            return resp;
        }
    }


    @PostMapping("/api/sessionPay")
    public ResponseData<SessionPayResp> requestSessionPay(@RequestBody String RSAEncryptedBase64String) {

        ResponseData<SessionPayResp> resp = new ResponseData<>();
        resp.data = new SessionPayResp();


        try {

            //check the request format
            var sessionRequest = sessionPayService.extractInfo(RSAEncryptedBase64String);
            if (sessionRequest == null) {
                resp.data.prompt = Prompt.session_pay_request_format_error;
                return resp;
            }

            //verify the signature
            Prompt prompt = sessionPayService.verifySessionRequest(sessionRequest);
            if (prompt != Prompt.success) {
                resp.data.prompt = prompt;
                return resp;
            }


            //initialize
            SessionPay sessionPay = sessionPayService.initialize(sessionRequest.merchantId, sessionRequest.amount);

            //initialize pay semaphore
            PaySemaphore paySemaphore = new PaySemaphore(sessionPay.sessionId);
            PaySemaphorePool.getInstance().add(paySemaphore);


            resp.data.sessionId = sessionPay.sessionId;
            resp.data.prompt = Prompt.success;

        } catch (Exception e) {
            e.printStackTrace();
            resp.data.prompt = Prompt.unknownError;
        }

        return resp;
    }


    @GetMapping("/api/payments/{pageNum}")
    public ResponseData<Page<PaymentWithRefund>> getAllPayments(@PathVariable int pageNum,
                                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                                                        int pageSize
    ) {


        ResponseData<Page<PaymentWithRefund>> resp = new ResponseData<>();

        try {

            resp.data = payService.getAllPays(pageSize, pageNum);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();

            resp.errorPrompt = ResponseData.unknownError;
            resp.status = ResponseData.ERROR;
            return resp;
        }
    }


    @PutMapping("/api/payment/state")
    public ResponseData<Prompt> refundPay(@RequestAttribute("userId") int userId, @RequestBody int paymentId) {

        ResponseData<Prompt> responseData = new ResponseData<>();

        try {
            Merchant merchant = merchantService.getMerchantByUserId(userId);
            responseData.data = payService.refundPayWithMerchantId(merchant.merchantId, paymentId);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }
    }


    @PostMapping("/api/refund")
    public ResponseData<Prompt> refundPayUsingRSA(@RequestBody String RSAEncryptedBase64String) {

        ResponseData<Prompt> responseData = new ResponseData<>();

        try {


            RefundRequest refundRequest = refundService.extractInfo(RSAEncryptedBase64String);

            if (refundRequest == null) {
                responseData.data = Prompt.refund_wrong_request_format;
                return responseData;
            }

            Prompt prompt = refundService.validateSignature(refundRequest);

            if (prompt != Prompt.success) {
                responseData.data = prompt;
                return responseData;
            }

            Merchant merchant = merchantService.getMerchantById(refundRequest.merchantId);


            return refundPay(merchant.merchantUserId, refundRequest.payId);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }
    }

}
