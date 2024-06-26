package com.springtest.demo.config;

import java.math.BigDecimal;

public class ConfigUtil {


    //RSA public and private key for server
    public static final String PRIVATE_KEY_BASE64_ENCODED = "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";
    public static final String PUBLIC_KEY_BASE64_ENCODED = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA3E7kJoxmPC1eckRVthTzoT+bbLsgh8X6K8CNtsUh6Oqf1v3MEUZCARZw9xmFV1quqbKXLuLiQqj86XF7DwqS4LU+5YvBB1qQ1h/rGOagN5qqThimXPGq3+bIZr2NDgMu4GkT4vXWJCYL7VnFfuIgjQH0aJQOrnZN6NBaoD0z5zLWL67v2VwDXDiNq61QEtMYfm1TcjBM0Q6MNyzPxSHHcJIg2IfwxoQbbka3wK6WrVs/5LZumkTnmUKS5h/zPQMjr5dwzCH9CVIO14v2yg8LC9lMXyaRaAdOEqRPddF/6O77dN6JcGGNjfYBFhO31Mkh5YXd6l/KAur2gSN742Uj8nKAWgTVCSlLZbOI5YJJigZoKDOAUMIPMFDEmKihznrzPmFQyPsOpQmNH0itRK6z9eVbJzDbhA1EYddXVvGegOi3veAZJfL+X/43KuPjbuSm6XAZC1ZIbTdPddX3vLYriMvc3omTk3Hx9wXDVRE6xuYJ/kwDbwDmT+PAXAxtLy8bfjEYf67w5JHvGFninS631eiOYneqmxndVBwvrGBJtm8kET2nZA+9Sk11DvZYU0jK2Wu+XXrywGBNgMSkcXmsVE4zfy82BAYAISKPIqpDbvFMKu3726mXReUVwJtNE712pkbQiOFhtEAlNw1IqEw+2M7EFBIFM/dM2K6KkCmwVTMCAwEAAQ==";

    // account for twilio.com
    public static final String ACCOUNT_SID = "AC88c3c0d971fb3a81e03277ba1960285a";
    public static final String AUTH_TOKEN = "53205c2146cda9b100ba5646de43a0e9";
    public static final String SID = "MG7b1df1f78ec5d3284e0ad0db49525d2c";


    //account for qq email
    public static final String USERNAME = "2449291739@qq.com";
    public static final String PASSWORD = "123456789";
    public static final String SMTP_HOST = "smtp.qq.com";
    public static final Integer SMTP_PORT = 587;


    public static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.01);

    public static final String MAX_AMOUNT_STR = "1e+20";
    public static final BigDecimal MAX_AMOUNT = new BigDecimal(MAX_AMOUNT_STR);

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";


}
