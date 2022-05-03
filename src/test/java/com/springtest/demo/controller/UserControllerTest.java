package com.springtest.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    void userRegister() {
    }

    @Test
    void userLogin() {

        var h = new HashMap<String, String>();
        h.put("phone", "+8619917910891");
        h.put("password", "123456");
        System.out.println(userController.userLogin(h));


    }

    @Test
    void getUserInfo() {

        System.out.println(userController.getUserInfo(12));


    }
}