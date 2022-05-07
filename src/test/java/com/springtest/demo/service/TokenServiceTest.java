package com.springtest.demo.service;

import com.springtest.demo.redisEntity.Token;
import com.springtest.demo.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Test
    void saveToken() {

        String tokenStr = Util.generateToken("user:" + "13");

        Token token = new Token();
        token.id = "13";
        token.token = tokenStr;

        tokenService.saveToken(token);
    }

    @Test
    void verify() {
    }
}