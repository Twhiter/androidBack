package com.springtest.demo.controller;


import com.springtest.demo.config.StaticFileConfig;
import com.springtest.demo.dto.*;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.enums.FileType;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.service.FileService;
import com.springtest.demo.service.MerchantService;
import com.springtest.demo.service.ServiceUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

@RestController
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @Autowired
    FileService fileService;

    @Autowired
    ServiceUtil serviceUtil;


    @GetMapping("/api/merchant")
    public ResponseData<Merchant> getMerchantByUserId(@RequestParam("userId") int userId) {

        ResponseData<Merchant> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = merchantService.getMerchantByUserId(userId);
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }

    @GetMapping("/api/merchant/{id}")
    public ResponseData<Merchant> getMerchantById(@PathVariable("id") int id) {

        ResponseData<Merchant> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = merchantService.getMerchantById(id);
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @GetMapping("/api/merchant/self")
    public ResponseData<Merchant> getMerchant(@ApiIgnore @RequestAttribute("userId") int userId) {
        return getMerchantByUserId(userId);
    }


    @GetMapping("/api/merchant/overview/{merchantId}")
    public ResponseData<OverviewInfo> getMerchantOverview(@PathVariable int merchantId) {

        ResponseData<OverviewInfo> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = merchantService.getMerchantOverview(merchantId);
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/merchant")
    public ResponseData<Prompt> merchantRegister(@ApiIgnore @RequestAttribute("userId") int userId,
                                                 @RequestParam("companyName") String companyName,
                                                 @RequestParam("licenseNumber") String licenseNumber,
                                                 @RequestParam("licensePhoto") MultipartFile licensePhoto,
                                                 @RequestParam(value = "phoneNumber", required = false, defaultValue = "")
                                                         String phoneNumber,
                                                 @RequestParam(value = "email", required = false, defaultValue = "")
                                                         String email) {


        ResponseData<Prompt> resp = new ResponseData<>();
        File f = null;

        try {

            Merchant merchant = new Merchant();

            merchant.merchantUserId = userId;
            merchant.merchantName = companyName;
            merchant.merchantLicense = licenseNumber;

            try {
                f = fileService.storeLicenseImage(licensePhoto);
            } catch (IOException e) {
                e.printStackTrace();
                resp.data = Prompt.unknownError;
                return resp;
            }

            merchant.merchantLicensePhoto = StaticFileConfig.toWebUrl(f.getName(), FileType.license_image);
            merchant.merchantPhoneNumber = phoneNumber;
            merchant.merchantEmail = email;

            resp.data = merchantService.registerMerchant(merchant);

            if (resp.data != null)
                f.delete();

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            if (f != null)
                f.delete();

            resp.data = Prompt.unknownError;
            return resp;
        }


    }


    @GetMapping("/api/merchants/{pageNumber}")
    public ResponseData<Page<UserAndMerchant>> getMerchants(@PathVariable int pageNumber,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                                                    int pageSize) {


        ResponseData<Page<UserAndMerchant>> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = merchantService.getMerchants(pageNumber, pageSize);
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }


    @PutMapping("/api/merchant/state")
    public ResponseData<Prompt> updateUserState(@RequestBody ModifyStateRequest obj) {

        ResponseData<Prompt> resp = new ResponseData<>();

        try {
            Merchant merchant = merchantService.getMerchantById(obj.id);

            switch (obj.state) {
                case frozen -> {
                    resp.data = merchantService.freezeMerchant(obj.id);
                    if (resp.data != Prompt.success)
                        return resp;

                    new Thread(() -> {
                        try {
                            serviceUtil.sendFrozenMessage(merchant.merchantEmail, merchant.merchantPhoneNumber, obj.reasons);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }).start();

                }
                case normal -> resp.data = merchantService.unfreezeMerchant(obj.id);
                case unverified -> throw new Exception();
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.data = Prompt.unknownError;
        }

        return resp;
    }


    @PutMapping("/api/merchant/frozenAmount")
    public ResponseData<Prompt> updateMerchantFrozenAmount(@RequestBody ModifyFrozenAmount obj) {

        ResponseData<Prompt> resp = new ResponseData<>();

        try {

            if (obj.amount.compareTo(BigDecimal.ZERO) > 0)
                resp.data = merchantService.freezeMerchantBalance(obj.id, obj.amount);
            else
                resp.data = merchantService.unfreezeMerchantBalance(obj.id, obj.amount.multiply(BigDecimal.valueOf(-1)));


            if (resp.data != Prompt.success)
                return resp;

            try {
                Merchant merchant = merchantService.getMerchantById(obj.id);
                new Thread(() -> {

                    try {
                        serviceUtil.sendFrozenBalanceMessage(merchant.merchantEmail, merchant.merchantPhoneNumber, obj.reasons, obj.amount);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
            resp.data = Prompt.unknownError;
        }

        return resp;
    }


    @GetMapping("/api/merchants/unverified/{pageNumber}")
    public ResponseData<Page<Merchant>> getUnverifiedMerchants(@PathVariable int pageNumber,
                                                               @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                                                       int pageSize
    ) {


        ResponseData<Page<Merchant>> resp = new ResponseData<>();
        try {
            resp.data = merchantService.getUnverifiedMerchants(pageSize, pageNumber);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.status = ResponseData.ERROR;
            return resp;
        }
    }

    @PutMapping("/api/merchant/unverified")
    public ResponseData<Prompt> acceptUser(@RequestBody int merchantId) {

        ResponseData<Prompt> resp = new ResponseData<>();

        try {
            resp.data = merchantService.acceptMerchant(merchantId);
            if (resp.data != Prompt.success)
                return resp;
            else {

                Merchant merchant = merchantService.getMerchantById(merchantId);

                new Thread(() -> {
                    try {
                        serviceUtil.sendAcceptMessage(merchant.merchantEmail, merchant.merchantPhoneNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                return resp;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.data = Prompt.unknownError;
            return resp;
        }

    }


    @PostMapping("/api/merchant/reject")
    public ResponseData<Prompt> rejectUser(@RequestBody RejectRequest obj) {

        ResponseData<Prompt> responseData = new ResponseData<>();


        try {

            Merchant merchant = merchantService.getMerchantById(obj.id);

            responseData.data = merchantService.deleteMerchant(obj.id);
            if (responseData.data != Prompt.success)
                return responseData;

            new Thread(() -> {

                try {
                    serviceUtil.sendRejectMessage(merchant.merchantEmail, merchant.merchantPhoneNumber, obj.reasons);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

            return responseData;

        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }

    }


}
