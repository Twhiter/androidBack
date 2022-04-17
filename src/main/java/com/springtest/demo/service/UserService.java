package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserDao userDao;


    public void register(User user) {
        userDao.insert(user);
    }


    public Map<String,Object> login(String phoneNumber, String password) {

        var s = new HashMap<String,Object>();

        User user = userDao.selectOne(new QueryWrapper<User>().
                eq("phone_number", phoneNumber)
                .eq("password", password)
        );

        s.put("user",user);

        if (user == null)
             s.put("prompt",Prompt.user_fail_login);
        else
            s.put("prompt",switch (user.state) {
            case frozen -> Prompt.user_account_frozen;
            case normal -> Prompt.use_successfully_login;
            case unverified -> Prompt.user_account_unverified;
        });

        return s;
    }

    private User getUser(int id) {
        return userDao.selectById(id);
    }

    public User getUserByAdmin(int id) {
        return getUser(id);
    }

    public User getUserByUser(int id) {
        User user = getUser(id);
        user.secureSet();
        return user;
    }


    public OverviewInfo getUserOverview(int id) {
        User user = getUser(id);
        return OverviewInfo.fromUser(user);
    }


    public boolean verifyPaymentPassword(String password,int userId) {
        return userDao.selectById(userId).paymentPassword.equals(password);
    }

}
