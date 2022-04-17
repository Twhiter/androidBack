package com.springtest.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PayControllerTest {

    @Autowired
    PayController payController;

    @Test
    void pay() {

//        System.out.println(payController.pay(13, 1, BigDecimal.ONE));


    }
}