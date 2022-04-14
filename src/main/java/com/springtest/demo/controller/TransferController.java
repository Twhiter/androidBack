package com.springtest.demo.controller;

import com.springtest.demo.dto.PayOverview;
import com.springtest.demo.dto.PayResp;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.dto.TransferResp;
import com.springtest.demo.entity.Pay;
import com.springtest.demo.entity.Transfer;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.service.TransferService;
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
public class TransferController {


    @Autowired
    TransferService transferService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/transfer")
    public ResponseData<TransferResp> transfer(@ApiIgnore @RequestAttribute("userId") int sourceId,
                                               @RequestParam(name = "targetUserId") int targetUserId,
                                               @RequestParam(name = "amount") BigDecimal amount) {

        amount = amount.setScale(4, RoundingMode.HALF_UP);
        ResponseData<TransferResp> responseData = new ResponseData<>();
        responseData.data = new TransferResp();

        try{
            var promptAndTransfer = transferService.transfer(sourceId, targetUserId, amount);
            responseData.data.prompt = (Prompt) promptAndTransfer[0];
            responseData.data.transfer = (Transfer) promptAndTransfer[1];
        }catch (Exception e) {
            e.printStackTrace();
            responseData.errorPrompt = "Error";
            responseData.status = ResponseData.ERROR;
        }
        return responseData;
    }








}
