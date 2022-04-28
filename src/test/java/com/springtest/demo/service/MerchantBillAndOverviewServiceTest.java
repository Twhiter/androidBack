package com.springtest.demo.service;

import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.enums.BillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SpringBootTest
class MerchantBillAndOverviewServiceTest {

    @Autowired
    MerchantBillAndOverviewService merchantBillAndOverviewService;

    @Test
    void getMerchantBillAndOverviewByPage() {
    }

    @Test
    void getMerchantBillRecordByPage() {


        List<BillType> list = new ArrayList<>();

        list.add(BillType.export_to_user);

        System.out.println(merchantBillAndOverviewService.getMerchantBillRecordByPage(1, 10, 1, BigDecimal.ZERO,
                ConfigUtil.MAX_AMOUNT, new Date(0), new Date(), list));
    }
}