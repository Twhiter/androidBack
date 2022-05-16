package com.springtest.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

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



}
