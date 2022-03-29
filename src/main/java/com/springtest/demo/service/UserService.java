package com.springtest.demo.service;

import com.springtest.demo.dao.UserDao;
import com.springtest.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;


    public void register(User user) {
        userDao.insert(user);
    }

}
