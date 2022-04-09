package com.springtest.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    void userRegister() {
    }

    @Test
    void userLogin() {
    }

    @Test
    void getUserInfo() {

        System.out.println(userController.getUserInfo(12));


    }
}