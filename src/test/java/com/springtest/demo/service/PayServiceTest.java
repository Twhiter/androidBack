package com.springtest.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayServiceTest {


    @Autowired
    PayService payService;


    @Test
    void pay() {

//        System.out.println(Arrays.toString(payService.pay(13, 1, BigDecimal.ONE)));
    }

    @Test
    void _pay() {
    }

    @Test
    void test() {

//        var s = payService.getAllPays(10, 1).get(0);
//        System.out.println(s.amount);
    }
}