package com.springtest.demo.controller;

import com.springtest.demo.dto.Page;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.dto.TransferResp;
import com.springtest.demo.entity.Transfer;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.service.TransferService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
                                               @RequestParam(name = "amount") BigDecimal amount,
                                               @RequestParam(name = "paymentPassword") String paymentPassword,
                                               @RequestParam(name = "remarks") String remarks) {

        amount = amount.setScale(4, RoundingMode.HALF_UP);
        ResponseData<TransferResp> responseData = new ResponseData<>();
        responseData.data = new TransferResp();

        try{
            var promptAndTransfer = transferService.transfer(sourceId, targetUserId, amount, paymentPassword, remarks);
            responseData.data.prompt = (Prompt) promptAndTransfer[0];
            responseData.data.transfer = (Transfer) promptAndTransfer[1];
        } catch (Exception e) {
            e.printStackTrace();
            responseData.errorPrompt = "Error";
            responseData.status = ResponseData.ERROR;
        }
        return responseData;
    }


    @GetMapping("/api/transfers/{pageNum}")
    public ResponseData<Page<Transfer>> getAllTransfers(@PathVariable int pageNum,
                                                        @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                                                int pageSize) {

        ResponseData<Page<Transfer>> resp = new ResponseData<>();

        try {

            resp.data = transferService.getAllTransfers(pageNum, pageSize);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = ResponseData.unknownError;
            return resp;
        }
    }
}
