package com.springtest.demo.service;


import com.springtest.demo.util.Util;
import com.springtest.demo.redisDao.EmailVerifyCodeDao;
import com.springtest.demo.redisDao.PhoneVerifyCodeDao;
import com.springtest.demo.redisEntity.EmailVerifyCode;
import com.springtest.demo.redisEntity.PhoneVerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;


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
