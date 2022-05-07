package com.springtest.demo.config;

import java.math.BigDecimal;

public class ConfigUtil {


    public static final String PRIVATE_KEY_BASE64_ENCODED = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAvNpxoeBddSLWaHR+j9gZJiYjJ1JzpNnZoxYJjJOuXKM0eIOynt/i48JBB4Y8ReaoKGQHBnu2RLhSHxWtR5h0awIDAQABAkA2w4+1Fed0CsyrELEMzGkKj3+dLQM8+DFs4fyV59NlqCvqOrru49RDqAnz5/kcrQlX2tjxKEzrXifVuQq1sQedAiEA8xT2PKh/AHy7FPLeGMq/xxnXWNw85fn7ipI/+EMIg08CIQDG47jtidKsnyfNkNJy/RfOLe0tz7T4EyTITl32STRmJQIgVeb6fcUt3IY3ttd9FVXRIBNJvuVVwA/vjLDSWnGV+JECIQCRM5PBJ8u85v0CIObjuE724aS0u11dd2x1KHGE6vDsWQIgJfJ7du95X39zVGf5cRibIn9ZfV3KdghfUhv8zpMz2Ww=";
    public static final String PUBLIC_KEY_BASE64_ENCODED = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALzacaHgXXUi1mh0fo/YGSYmIydSc6TZ2aMWCYyTrlyjNHiDsp7f4uPCQQeGPEXmqChkBwZ7tkS4Uh8VrUeYdGsCAwEAAQ==";


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
