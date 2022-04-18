package com.springtest.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void encodeEmail() {

        System.out.println(Util.encodeEmail("542@qq.com"));


    }

    @Test
    void encodePhone() {

        System.out.println(Util.encodePhone("+375445520140"));

    }
}