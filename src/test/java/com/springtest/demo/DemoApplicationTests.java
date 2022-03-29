package com.springtest.demo;


import com.springtest.demo.dao.PhoneVerifyCodeDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.entity.User;
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

    @Autowired
    UserDao userDao;


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

    @Test
    public void test3() {

        User user = new User();

        user.avatar = "/test";
        user.country = "China";
        user.firstName = "Whiter";
        user.lastName = "Tu";
        user.email = "1065582542@qq.com";
        user.passportNumber = "passportNumber";
        user.password = "123";
        user.passportPhoto = "/photo";
        user.paymentPassword = "123";
        user.phoneNumber = "+375445520140";

        userDao.insert(user);

//        var u = userDao.selectList(null).get(0);
//
//
//        System.out.println(u);


    }
}
