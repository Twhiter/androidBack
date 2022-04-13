package com.springtest.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void register() {
    }

    @Test
    void login() {
    }

    @Test
    void getUserByAdmin() {
    }

    @Test
    void getUserByUser() {
    }

    @Test
    void getUserOverview() {
        System.out.println(userService.getUserOverview(12));
    }
}