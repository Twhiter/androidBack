package com.springtest.demo.controller;


import com.springtest.demo.dto.PayOverview;
import com.springtest.demo.dto.PayResp;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.entity.Pay;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.service.PayService;
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

@RestController
public class PayController {



    @Autowired
    private PayService payService;



    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/pay")
    public ResponseData<PayResp> pay(@ApiIgnore @RequestAttribute("userId") int userId,
                                     @RequestParam(name = "merchantId") int merchantId,
                                     @RequestParam(name = "amount")BigDecimal amount) {

        amount = amount.setScale(4, RoundingMode.HALF_UP);
        ResponseData<PayResp> responseData = new ResponseData<>();
        responseData.data = new PayResp();


        try{
            var promptAndPay = payService.pay(userId, merchantId, amount);
            responseData.data.prompt = (Prompt) promptAndPay[0];
            responseData.data.payOverview = PayOverview.fromPay((Pay) promptAndPay[1]);
        }catch (Exception e) {
            e.printStackTrace();
            responseData.errorPrompt = "Error";
            responseData.status = ResponseData.ERROR;
        }
        return responseData;
    }
}
