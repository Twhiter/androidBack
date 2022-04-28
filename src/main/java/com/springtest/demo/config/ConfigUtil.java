package com.springtest.demo.config;

import java.math.BigDecimal;

public class ConfigUtil {


    // account for twilio.com
    public static final String ACCOUNT_SID = "AC88c3c0d971fb3a81e03277ba1960285a";
    public static final String AUTH_TOKEN = "53205c2146cda9b100ba5646de43a0e9";
    public static final String SID = "MG7b1df1f78ec5d3284e0ad0db49525d2c";


    //account for qq email
    public static final String USERNAME = "2449291739@qq.com";
    public static final String PASSWORD = "bjuvzmtmzcvkdhid";
    public static final String SMTP_HOST = "smtp.qq.com";
    public static final Integer SMTP_PORT = 587;


    public static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.01);

    public static final String MAX_AMOUNT_STR = "1e+20";
    public static final BigDecimal MAX_AMOUNT = new BigDecimal(MAX_AMOUNT_STR);

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";


}
