package com.springtest.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BillControllerTest {


    @Autowired
    BillController billController;

    @Test
    void getUserBills() {


//        System.out.println(billController.getUserBills(12, 1, 1, BigDecimal.ZERO, ConfigUtil.MAX_AMOUNT,
//                Collections.singletonList(BillType.transfer_out)));

    }

    @Test
    void getMerchantBills() {

//        System.out.println(billController.getMerchantBills(12, 1, 1, BigDecimal.ZERO, ConfigUtil.MAX_AMOUNT,
//                Collections.singletonList(BillType.pay)));


    }
}