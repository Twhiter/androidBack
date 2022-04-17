package com.springtest.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TransferServiceTest {

    @Autowired
    TransferService transferService;

    @Test
    void transfer() {


//        System.out.println(Arrays.toString(transferService.transfer(13, 12, BigDecimal.ONE)));


    }
}