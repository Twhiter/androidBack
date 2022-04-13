package com.springtest.demo.controller;


import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.service.MerchantService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class MerchantController {

    @Autowired
    MerchantService merchantService;


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
        }catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }



}
