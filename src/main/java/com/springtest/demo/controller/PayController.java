package com.springtest.demo.controller;


import com.springtest.demo.businessEntity.PaySemaphore;
import com.springtest.demo.businessEntity.PaySemaphorePool;
import com.springtest.demo.dto.MerchantVerifyInfo;
import com.springtest.demo.dto.PayOverview;
import com.springtest.demo.dto.PayResp;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.entity.Pay;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.redisEntity.SessionPay;
import com.springtest.demo.service.PayService;
import com.springtest.demo.service.PayVerifyService;
import com.springtest.demo.service.SessionPayService;
import com.springtest.demo.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    private UserService userService;

    @Autowired
    private PayVerifyService payVerifyService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/pay")
    public ResponseData<PayResp> pay(@ApiIgnore @RequestAttribute("userId") int userId,
                                     @RequestParam(name = "merchantId") int merchantId,
                                     @RequestParam(name = "amount") BigDecimal amount,
                                     @RequestParam(name = "paymentPassword")String paymentPassword,
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

    //todo need a test
    @PostMapping("/api/payVerify")
    public ResponseData<Prompt> verifyPay(@RequestParam(name = "body") String RSAEncryptedBase64String) {


        ResponseData<Prompt> resp = new ResponseData<>();
        resp.data = Prompt.unknownError;

        try {
            MerchantVerifyInfo verifyInfo = payVerifyService.extractInfo(RSAEncryptedBase64String);
            if (verifyInfo == null) {
                resp.data = Prompt.pay_verify_request_format_error;
                return resp;
            }

            SessionPay sessionPay = sessionPayService.getById(verifyInfo.sessionId);
            if (sessionPay == null) {
                resp.data = Prompt.pay_time_out;
                return resp;
            }

            PaySemaphore paySemaphore = PaySemaphorePool.getInstance().get(sessionPay.sessionId);
            if (paySemaphore == null || paySemaphore.notScanned.availablePermits() == 0) {
                resp.data = Prompt.pay_time_out;
                return resp;
            }

            Prompt prompt = payVerifyService.verify(verifyInfo, sessionPay);
            if (prompt != Prompt.success) {
                resp.data = prompt;
                return resp;
            }


            Object[] promptAndPay = payService.payWithConfirm(sessionPay);
            prompt = (Prompt) promptAndPay[0];
            Pay pay = (Pay) promptAndPay[1];


            PayResp payResp = new PayResp();
            payResp.prompt = prompt;
            payResp.payOverview = PayOverview.fromPay(pay);

            //assign the payment result to paySyn data
            paySemaphore.paySynData.payResp = payResp;

            paySemaphore.isFinished.release();

            resp.data = prompt;
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.data = Prompt.unknownError;
            return resp;
        }
    }
}
