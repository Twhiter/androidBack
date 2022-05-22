package com.springtest.demo.util;

import org.junit.jupiter.api.Test;

import java.util.Base64;

class RSAUtilTest {

    @Test
    void generateRsaKey() {


        var p = RSAUtil.generateRsaKey();


        assert p != null;
        var publicKey = p.getPublic();
        var privateKey = p.getPrivate();


        System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

    }
}