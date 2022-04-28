package com.springtest.demo.service;

import com.springtest.demo.enums.BillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class UserBillAndOverviewServiceTest {

    @Autowired
    UserBillAndOverviewService userBillAndOverviewService;

    @Test
    void getUserBillAndOverviewByPage() {
    }

    @Test
    void getUserBillRecordByPage() {


        List<BillType> list = new ArrayList<>();
        list.add(BillType.pay);

//        System.out.println(userBillAndOverviewService.getUserBillRecordByPage(13, 10, 1, BigDecimal.ZERO,
//                ConfigUtil.MAX_AMOUNT, list));


    }
}