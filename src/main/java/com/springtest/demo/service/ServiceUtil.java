package com.springtest.demo.service;


import com.springtest.demo.redisDao.EmailVerifyCodeDao;
import com.springtest.demo.redisDao.PhoneVerifyCodeDao;
import com.springtest.demo.redisEntity.EmailVerifyCode;
import com.springtest.demo.redisEntity.PhoneVerifyCode;
import com.springtest.demo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigDecimal;


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


    public boolean checkPhoneVerifyCode(String phone, String verifyCode) {
        var result = phoneVerifyCodeDao.findById(phone);
        return result.map(phoneVerifyCode -> phoneVerifyCode.getCode().equals(verifyCode)).orElse(false);
    }

    public void sendFrozenMessage(String email, String telephone, String reasons) throws MessagingException {


        String text = String
                .format("We are sorry to inform you that your account is frozen for these reasons:\n%s", reasons);
        String title = "Your account is frozen";

        Util.sendEmail(email, text, title);
        Util.sendSMSText(telephone, text);

    }

    public void sendFrozenBalanceMessage(String email, String telephone, String reasons, BigDecimal amount) throws MessagingException {

        String text = String
                .format("We are sorry to inform you that %s BYN are frozen in your account for these reasons:\n%s",
                        amount.toString(), reasons);
        String title = "Your balance is frozen";

        Util.sendEmail(email, text, title);
        Util.sendSMSText(telephone, text);


    }


}
