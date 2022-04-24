package com.springtest.demo;


import com.springtest.demo.dao.MerchantBillDao;
import com.springtest.demo.dao.UserBillDao;
import com.springtest.demo.redisDao.PhoneVerifyCodeDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.entity.User;
import com.springtest.demo.redisDao.TokenDao;
import com.springtest.demo.redisEntity.Token;
import com.springtest.demo.service.ServiceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.CharBuffer;


@SpringBootTest

public class DemoApplicationTests {


    @Autowired
    PhoneVerifyCodeDao phoneVerifyCodeDao;

    @Autowired
    ServiceUtil serviceUtil;

    @Autowired
    UserDao userDao;

    @Autowired
    UserBillDao userBillDao;

    @Autowired
    MerchantBillDao merchantBillDao;


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


    @Test
    public void test4() throws IOException {

        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader();


        pipedWriter.connect(pipedReader);


        pipedWriter.write("It's Okay");


        CharBuffer charBuffer = CharBuffer.allocate(64);
        pipedReader.read(charBuffer);

        System.out.println(charBuffer);
    }



    @Autowired
    TokenDao tokenDao;

    @Test
    public void test5() {

//        Token token = new Token();
//        token.userId = 1;
//        token.token = "123";
//
//        tokenDao.save(token);


        tokenDao.findById(1).ifPresent(System.out::println);

        tokenDao.findByToken("123").forEach(System.out::println);
    }



    @Test
    public void test6() {

        System.out.println(userBillDao.selectList(null));
        System.out.println(merchantBillDao.selectList(null));

    }



}
