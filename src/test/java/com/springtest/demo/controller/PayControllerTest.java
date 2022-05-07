package com.springtest.demo.controller;

import com.springtest.demo.businessEntity.PaySemaphore;
import com.springtest.demo.businessEntity.PaySemaphorePool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayControllerTest {

    @Autowired
    PayController payController;

    @Test
    void pay() {

//        System.out.println(payController.pay(13, 1, BigDecimal.ONE));


    }

    @Test
    void payWithConfirm() throws InterruptedException {

        PaySemaphorePool pool = PaySemaphorePool.getInstance();
        PaySemaphore paySemaphore = new PaySemaphore(1);

        pool.add(paySemaphore);
        System.out.println(payController.payWithConfirm(12, 1, "123", "123"));


    }
}