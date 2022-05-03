package com.springtest.demo.controller;


import com.springtest.demo.config.StaticFileConfig;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.enums.FileType;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.service.FileService;
import com.springtest.demo.service.MerchantService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;

@RestController
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @Autowired
    FileService fileService;


    @GetMapping("/api/merchant")
    public ResponseData<Merchant> getMerchantByUserId(@RequestParam("userId") int userId) {

        ResponseData<Merchant> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = merchantService.getMerchantByUserId(userId);
        }catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }

    @GetMapping("/api/merchant/{id}")
    public ResponseData<Merchant> getMerchantById(@PathVariable("id")int id) {

        ResponseData<Merchant> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = merchantService.getMerchantById(id);
        }catch (Exception e) {
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


}
