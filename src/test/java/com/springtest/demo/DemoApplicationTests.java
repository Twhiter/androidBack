package com.springtest.demo;


import com.springtest.demo.dao.PhoneVerifyCodeDao;
import com.springtest.demo.service.ServiceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
public class DemoApplicationTests {


    @Autowired
    PhoneVerifyCodeDao phoneVerifyCodeDao;

    @Autowired
    ServiceUtil serviceUtil;


    @Test
    public void test() throws MessagingException {

        serviceUtil.sendEmailVerifyCode("whiterq6@gmail.com");
        serviceUtil.sendPhoneVerifyCode("110");
    }


    @Test
    public void test2() {

        System.out.println(serviceUtil.checkPhoneVerifyCode("110", "242935"));
        System.out.println(serviceUtil.checkEmailVerifyCode("whiterq6@gmail.com", "811220"));

    }
}
