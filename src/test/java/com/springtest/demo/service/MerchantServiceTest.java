package com.springtest.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MerchantServiceTest {


    @Autowired
    MerchantService merchantService;

    @Test
    void getMerchantOverview() {


        System.out.println(merchantService.getMerchantOverview(1));


    }
}