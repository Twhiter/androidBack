package com.springtest.demo.controller;


import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.dto.BillRecord;
import com.springtest.demo.dto.Page;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.enums.BillType;
import com.springtest.demo.service.MerchantBillAndOverviewService;
import com.springtest.demo.service.UserBillAndOverviewService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
public class BillController {


    @Autowired
    MerchantBillAndOverviewService merchantBillAndOverviewService;

    @Autowired
    UserBillAndOverviewService userBillAndOverviewService;


    @GetMapping("/api/bills/user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    public ResponseData<Page<BillRecord>> getUserBills(@ApiIgnore @RequestAttribute("userId") int userId,
                                                       @RequestParam("pageSize") int pageSize,
                                                       @RequestParam("pageNum") int pageNum,
                                                       @RequestParam(value = "min", required = false, defaultValue = "0")
                                                               BigDecimal min,
                                                       @RequestParam(value = "max", required = false,
                                                               defaultValue = ConfigUtil.MAX_AMOUNT_STR) BigDecimal max,
                                                       @RequestParam(value = "start", required = false,
                                                               defaultValue = "#{new java.util.Date(0)}")
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               Date start,
                                                       @RequestParam(value = "end", required = false,
                                                               defaultValue = "#{new java.util.Date()}")
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               Date end,
                                                       @RequestParam("billTypes") List<BillType> requestedBillTypes) {

        ResponseData<Page<BillRecord>> resp = new ResponseData<>();

        try {
            resp.data = userBillAndOverviewService.
                    getUserBillRecordByPage(userId, pageSize, pageNum, min, max, start, end, requestedBillTypes);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error!";
            return resp;
        }
    }

    @GetMapping("/api/bills/merchant")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    public ResponseData<Page<BillRecord>> getMerchantBills(@ApiIgnore @RequestAttribute("userId") int userId,
                                                           @RequestParam("pageSize") int pageSize,
                                                           @RequestParam("pageNum") int pageNum,
                                                           @RequestParam(value = "min", required = false,
                                                                   defaultValue = "0") BigDecimal min,
                                                           @RequestParam(value = "max", required = false,
                                                                   defaultValue = ConfigUtil.MAX_AMOUNT_STR) BigDecimal max,
                                                           @RequestParam(value = "start", required = false,
                                                                   defaultValue = "#{new java.util.Date(0)}")
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                   Date start,
                                                           @RequestParam(value = "end", required = false,
                                                                   defaultValue = "#{new java.util.Date}")
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                   Date end,
                                                           @RequestParam("billTypes") List<BillType> requestedBillTypes) {

        ResponseData<Page<BillRecord>> resp = new ResponseData<>();

        try {
            resp.data = merchantBillAndOverviewService.
                    getMerchantBillRecordByPageWithUserId(userId, pageSize, pageNum, min, max, start, end, requestedBillTypes);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error!";
            return resp;
        }
    }
}
