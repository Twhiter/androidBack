package com.springtest.demo.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.AdminDao;
import com.springtest.demo.entity.Admin;
import com.springtest.demo.enums.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {


    @Autowired
    AdminDao adminDao;



    public Prompt login(Admin admin) {

        return ! adminDao.exists(new QueryWrapper<Admin>()
                .eq("admin_account",admin.adminAccount)
                .eq("password",admin.password))
                ? Prompt.admin_fail_login : Prompt.admin_successfully_login;
    }
}
