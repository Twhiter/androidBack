package com.springtest.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtest.demo.businessEntity.PaySemaphore;
import com.springtest.demo.businessEntity.PaySemaphorePool;
import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.controller.PayController;
import com.springtest.demo.dao.MerchantBillDao;
import com.springtest.demo.dao.MerchantRsaPublicKeyDao;
import com.springtest.demo.dao.UserBillDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dto.MerchantVerifyInfo;
import com.springtest.demo.dto.PayResp;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.dto.UserAndMerchant;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.redisDao.PhoneVerifyCodeDao;
import com.springtest.demo.redisDao.TokenDao;
import com.springtest.demo.service.PayVerifyService;
import com.springtest.demo.service.ServiceUtil;
import com.springtest.demo.service.SessionPayService;
import com.springtest.demo.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;


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

    @Autowired
    SessionPayService sessionPayService;

    @Autowired
    MerchantRsaPublicKeyDao merchantRsaPublicKeyDao;

    @Autowired
    PayVerifyService payVerifyService;

    @Autowired
    PayController payController;


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


        tokenDao.findById("1").ifPresent(System.out::println);

        tokenDao.findByToken("123").forEach(System.out::println);
    }


    @Test
    public void test6() {

        System.out.println(userBillDao.selectList(null));
        System.out.println(merchantBillDao.selectList(null));

    }

    @Test
    public void test7() throws InterruptedException, JsonProcessingException {


        //initialize a session pay
        var sessionPay = sessionPayService.initialize(1, BigDecimal.ONE);
        var pool = PaySemaphorePool.getInstance();

        //add control sempaphore
        PaySemaphore paySemaphore = new PaySemaphore(sessionPay.sessionId);
        pool.add(paySemaphore);

        var privateKeyBase64 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCqwmFhms6b/0kDdVt3c50v4R4i6ftt6Zu6AxwfEOLXOQ6l/I/IZHObEJf7jzQ+0QXYAtYG8xkT3Y2xNdsA0Ri3fBmyhBv6Ph4WPz9OK44sDiiBDZ0NQfM3IZdv2QwkhJOtHCvgOkHjTJRULyx6J/s41VDwcAO3se8U1cTKoWldoGn7qseuOc86nhs/QxYEZf9W4frghf9YNvI0cDwAgGKM13Rj5ENqRZED9y2PfepsoFfLwNo++0tq+W7dL0BnRG7ofEoqAxE/4ROZi05ibli9X9hZxCVM29HWPNuFbm7oafB371cGnXMv1h0vAZaVpzNtO+ssmpxZwryyWoObCK9VAgMBAAECggEAF7Iyvk8zxa0W7j5ua3SlRvwn6PNsWzPUUutUjzESMYiu8jTKpSXfbxDN7nBQcTkjZPb7yWoZvEp9+638DrB1jBzSGhZAJalSFoc/WzMrHwz17Pa9KRZSUH++WpyVlUm6joa+xfZ/pzG80bPcKbV7pMbhXQRGV8r+4PbLQM1GaGH8wDwtCnct29oEK/rSPHHnzYM4GqPm0LBeFeDEkZFtqDOP/8PA7ZfHSiUn77syS0elEtuWXUaKvjmACOU88rrVP/f8FUY5LjkBIkLEziAsCCytXWUNJgBra0R3UAfI72K9WuJAR0NnkTnrboUZ1TaYAMtuCBYqsnRo0nrcKBUOXwKBgQDEuKj/LUqQUrMBHg3UCIXCqevJmWUhii9/D9omvkFwCpTx9L9YMx4xiA7rHyF3VB2x+oqm/dhn7YNNm99IfRymyZA0sSOe/DAMn7HT43FhHYZrJ4qz3Z/5w2BChMhf5Or9ruh3BmzhrH4aD3MVQQyCW05omMjE/C6jJ3YWY7UAKwKBgQDeNvmyXE6BqP68OaQ27Y7obad8JMz4Te62mESp9bkmOo70yznl++w4rtSCwySr/9bt+hC2tqyw4o0iYk8rqtOaYmqnY3/MNzdiykfZMR1rIz4OxMxH0JznUzo0WzcGv0WsNmkRXAGXuvPInhXAKf0qGWrR2B4L37Ux78ltkdHOfwKBgGoiLpsttlLTK1xo+JDy4Ce8QUgNCHDl+uYUiV/puahXLTC1GR7exWV3wIS90PJuYW8Kew/JWJ4JmJOgiwCy72hUywaCo3M9IDdPWUO2LA93PHRHax/LbGn2LNL2jJygBuBDI4xD3S6MP6STvUU0SSlzVYQ9GeSdIhRIT/+YX6jtAoGBANjs3/rfI4Bt4icLOiSo6Y46pXqKTlHDelpj8Pvdtd384ltUHPRHZsW63he8uorx2PY93J0RXSncL5y2TcU6IRGgK6+2cSaBbJ8TCuqaiLZzE/zE3wroOpOT6sj7lo8On8xK3Wyl3aQqTBu3P1J1av/zChVKHaLHwxG5nHBLHn6vAoGAIO22rPoPK509CpJ684eeeYX9d+Qqa6WNc2UOoyIZ0X4of0Ru9sFFz9LSp3E+KtT1DdCUdDVhXJFCN2Gh/WCoj9Ckk3L6uOZDUb87Xzw/COziXQqqE/DdMH4923FOAeirea/UQdA4t+5REc6w4Bar0owv6Hu5Ky4qOybsdGJEeRU=";


        //simulate verify sending data
        MerchantVerifyInfo verifyInfo = new MerchantVerifyInfo();
        verifyInfo.sessionId = sessionPay.sessionId;
        var sigBytes = RSAUtil.sign(sessionPay.sessionId.toString().getBytes(StandardCharsets.UTF_8), privateKeyBase64);
        verifyInfo.signature = Base64.encodeBase64String(sigBytes);

        var bytes = new ObjectMapper().writeValueAsBytes(verifyInfo);

        var jsonStrBytes = RSAUtil.encrypt(bytes, ConfigUtil.PUBLIC_KEY_BASE64_ENCODED);
        var jsonStr = Base64.encodeBase64String(jsonStrBytes);


        AtomicReference<ResponseData<PayResp>> responseData = new AtomicReference<>(new ResponseData<>());

        Thread phoneScan = new Thread(() -> responseData.set(payController.payWithConfirm(13, sessionPay.sessionId,
                "111111", "")));


        AtomicReference<ResponseData<Prompt>> promptAtomicReference = new AtomicReference<>();

        Thread verify = new Thread(() -> promptAtomicReference.set(payController.verifyPay(jsonStr)));


        phoneScan.start();

        Thread.sleep(10000);

        verify.start();

        phoneScan.join();
        verify.join();


        System.out.println(responseData.get());
        System.out.println(promptAtomicReference.get());

    }


    @Test
    public void test8() {

        var s = userDao.getMerchantAndUser(10, 1);

        System.out.println(s.stream().map(UserAndMerchant::fromUserJoinMerchant).toList());


    }
}
