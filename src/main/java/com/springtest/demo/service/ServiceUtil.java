package com.springtest.demo.service;


import com.springtest.demo.Util;
import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.dao.EmailVerifyCodeDao;
import com.springtest.demo.dao.PhoneVerifyCodeDao;
import com.springtest.demo.redisEntity.EmailVerifyCode;
import com.springtest.demo.redisEntity.PhoneVerifyCode;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class ServiceUtil {

    @Autowired
    private PhoneVerifyCodeDao phoneVerifyCodeDao;

    @Autowired
    private EmailVerifyCodeDao emailVerifyCodeDao;


    public void sendPhoneVerifyCode(String phoneNumber) {

        String randomCode = Util.generateRandomCode(6);
        PhoneVerifyCode phoneVerifyCode = new PhoneVerifyCode();
        phoneVerifyCode.setCode(randomCode);
        phoneVerifyCode.setPhone(phoneNumber);

        phoneVerifyCodeDao.save(phoneVerifyCode);

        Util.sendSMSText(phoneNumber,randomCode);
    }



    public void sendEmailVerifyCode(String email) throws MessagingException {

        String randomCode = Util.generateRandomCode(6);
        EmailVerifyCode emailVerifyCode = new EmailVerifyCode();
        emailVerifyCode.setEmail(email);
        emailVerifyCode.setCode(randomCode);

        emailVerifyCodeDao.save(emailVerifyCode);

        Util.sendEmail(email, randomCode,"VerifyCode");
    }


    /***
     * check the input veri
     * @param email email
     * @param verifyCode verifyCode to be checked
     * @return
     */
    public boolean checkEmailVerifyCode(String email,String verifyCode) {

        var result = emailVerifyCodeDao.findById(email);
        return result.map(emailVerifyCode -> emailVerifyCode.getCode().equals(verifyCode)).orElse(false);
    }


    public boolean checkPhoneVerifyCode(String phone,String verifyCode) {
        var result = phoneVerifyCodeDao.findById(phone);
        return result.map(phoneVerifyCode -> phoneVerifyCode.getCode().equals(verifyCode)).orElse(false);
    }
}
