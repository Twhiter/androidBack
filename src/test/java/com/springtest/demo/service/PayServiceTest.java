package com.springtest.demo.service;

import com.springtest.demo.enums.Prompt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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
}