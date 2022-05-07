package com.springtest.demo.redisDao;

import com.springtest.demo.redisEntity.SessionPay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class SessionPayDaoTest {

    @Autowired
    SessionPayDao sessionPayDao;


    @Test
    public void test() {
        SessionPay sessionPay = new SessionPay();
        sessionPay.merchantId = 1;
        sessionPay.userId = 1;

        sessionPay.amount = BigDecimal.ONE;
        sessionPay.remarks = "213";

        var s = sessionPayDao.save(sessionPay);

        System.out.println(s);


    }

}