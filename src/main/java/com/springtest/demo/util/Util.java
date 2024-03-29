package com.springtest.demo.util;

import com.springtest.demo.config.ConfigUtil;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class Util {


    //generate random codes
    public static String generateRandomCode(int length) {

        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }


    public static void sendSMSText(String phoneNumber,String text) {


        Twilio.init(ConfigUtil.ACCOUNT_SID, ConfigUtil.AUTH_TOKEN);
        Message.creator(
                new PhoneNumber("+375445520140"),
                ConfigUtil.SID,
                text
        ).create();
    }


    public static void sendEmail(String to,String content,String title) throws MessagingException {

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.host", ConfigUtil.SMTP_HOST);
        prop.put("mail.smtp.port", ConfigUtil.SMTP_PORT);

        // SSL Factory
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");


        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ConfigUtil.USERNAME, ConfigUtil.PASSWORD);
            }
        });


        javax.mail.Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(ConfigUtil.USERNAME));
        message.setRecipients(javax.mail.Message.RecipientType.TO,InternetAddress.parse(to));
        message.setContent(content,"text/plain;charset=utf-8");
        message.setSubject(title);

        Transport.send(message);
    }


    public static String generateRandomFileName() {
        return UUID.randomUUID().toString();
    }

    public static String generateToken(String id) {
        return UUID.randomUUID().toString() + UUID.nameUUIDFromBytes(id.getBytes(StandardCharsets.UTF_8)).toString();
    }


    //encode email into format like ***asd@aa.com
    public static String encodeEmail(String email) {

        int idx = email.lastIndexOf("@");
        String encoded;

        String partBeforeAlpha = email.substring(0,idx);
        if (partBeforeAlpha.length() > 3)
            encoded = partBeforeAlpha.substring(0,partBeforeAlpha.length() - 3) + "***";
        else
            encoded = partBeforeAlpha.replaceAll(".","*");

        return encoded + email.substring(idx);
    }


    public static String encodePhone(String phone) {


        int idx = phone.length() - 4;
        return phone.substring(0, idx) + "****";
    }


    public static boolean isInteger(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
