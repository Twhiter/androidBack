package com.springtest.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JWTUtil {


    private static final String secret = "U3VycHJpc2UgTW90aGVyIGZ1Y2tlcg==";





    public static String sign(String content, Date expireAt) {
        return JWT.create()
                .withExpiresAt(expireAt)
                .withClaim("data",content)
                .sign(Algorithm.HMAC256(secret));
    }


    public static DecodedJWT verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            return verifier.verify(token);

        } catch (JWTVerificationException exception){
            return null;
        }
    }


    // The code is not necessary

//    public static String signPaymentToken(int userId) {
//
//        UserPaymentTokenContent userPaymentTokenContent = new UserPaymentTokenContent();
//        userPaymentTokenContent.userId = userId;
//
//        try {
//            String jsonVal = new ObjectMapper().writeValueAsString(userPaymentTokenContent);
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MINUTE,2);
//            Date expireAt = calendar.getTime();
//
//            return sign(jsonVal,expireAt);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//
//
//    public static boolean verifyPaymentToken(int userId,String token) {
//
//        var decodedJWT = verify(token);
//
//        if (decodedJWT == null)
//            return false;
//
//
//
//        String  jsonStr = decodedJWT.getClaim("data").asString();
//        try {
//            var tokenContent = new ObjectMapper().readValue(jsonStr,UserPaymentTokenContent.class);
//
//            return tokenContent.userId == userId && tokenContent.operation == Operation.Payment;
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//
//}
//
//@Data
//class UserPaymentTokenContent {
//
//    @JsonProperty("userId") public int userId;
//    @JsonProperty("operation") public Operation operation = Operation.Payment;
//}
//
//enum Operation {
//
//    Payment("payment");
//
//    @JsonValue
//    public String value;
//
//     Operation(String s) {
//        value = s;
//    }
}//
