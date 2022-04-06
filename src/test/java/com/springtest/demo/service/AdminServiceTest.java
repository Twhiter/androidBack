package com.springtest.demo.service;

import com.springtest.demo.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Test
    void login() {


        Admin admin = new Admin();
        admin.adminAccount = "123";
        admin.password = "RTWhiter12";
        System.out.println(adminService.login(admin));

    }
}