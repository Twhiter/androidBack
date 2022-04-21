package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.dto.Page;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.util.Util;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

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

    public OverviewInfo getUserOverviewInfoByPhone(String phoneNumber) {
        User user =  userDao
                .selectOne(new QueryWrapper<User>().eq("phone_number",phoneNumber));
        return user == null ? null : OverviewInfo.fromUser(user);
    }

    public Page<OverviewInfo> getUserOverviewInfosByEmail(String email,int currentPage,int pageSize) {

        List<User> users = userDao.selectList(new QueryWrapper<User>()
                .eq("email",email)
                .last(String.format("limit %d,%d",(currentPage - 1) * pageSize,pageSize))
        );

        int count = Math.toIntExact(userDao.selectCount(new QueryWrapper<User>().eq("email",email)));

        int maxPage = (int) Math.ceil(1.0 * count / pageSize);
        var data =  users.stream().map(OverviewInfo::fromUser).toList();

        return new Page<>(currentPage,maxPage,pageSize,data);
    }


    public Page<OverviewInfo> getUserOverviewInfoByNameRoughly(String roughName, int page, int pageSize) {

        List<User>users = userDao.selectList(new QueryWrapper<User>()
                .apply("concat(lower(first_name),' ',lower(last_name)) like {0}"
                        ,"%" + roughName.toLowerCase() + "%")
                .last(String.format("limit %d,%d",(page - 1) * pageSize,pageSize))
        );

        int count = Math.toIntExact(userDao.selectCount(new QueryWrapper<User>()
                .apply("concat(lower(first_name),' ',lower(last_name)) like {0}"
                        , "%" + roughName.toLowerCase() + "%")));

        int maxPage = (int) Math.ceil(1.0 * count / pageSize);

        var data =  users.stream().map(OverviewInfo::fromUser).toList();

        Page<OverviewInfo> pageObj = new Page<>();
        pageObj.maxPage = maxPage;
        pageObj.pageSize = pageSize;
        pageObj.currentPage = page;
        pageObj.data = data;

        return pageObj;
    }


    public OverviewInfo getUserOverviewInfoByPhoneEncoded(String phoneNumber) {
        var overviewInfo =  getUserOverviewInfoByPhone(phoneNumber);
        overviewInfo.email = Util.encodeEmail(overviewInfo.email);
        return overviewInfo;
    }

    public Page<OverviewInfo> getUserOverviewInfosByEmailEncoded(String email,int currentPage,int pageSize) {

        var page = getUserOverviewInfosByEmail(email,currentPage,pageSize);
         page.data = page.data.stream()
                .peek(overviewInfo -> overviewInfo.phoneNumber = Util.encodePhone(overviewInfo.phoneNumber))
                .toList();
         return page;
    }


    public Page<OverviewInfo> getUserOverviewInfoByNameRoughlyEncoded(String roughName,int page,int count) {

        var pageObj = getUserOverviewInfoByNameRoughly(roughName,page,count);

        pageObj.data = pageObj.data.stream()
                .peek(overviewInfo -> {
                    overviewInfo.phoneNumber = Util.encodePhone(overviewInfo.phoneNumber);
                    overviewInfo.email = Util.encodeEmail(overviewInfo.email);
                })
                .toList();

        return pageObj;
    }


    public Page<OverviewInfo> searchUser(String keyword,int page,int count) {

        Page<OverviewInfo> pageObj = new Page<>();
        pageObj.data = new ArrayList<>();
        pageObj.currentPage = page;
        pageObj.maxPage = 1;
        pageObj.pageSize = count;

        if (EmailValidator.getInstance().isValid(keyword)) {
            pageObj = getUserOverviewInfosByEmailEncoded(keyword.toLowerCase(),page,count);
        }else if (keyword.matches("\\+\\d+")) {
            var info = this.getUserOverviewInfoByPhoneEncoded(keyword.toLowerCase());
            if (info != null)
                pageObj.data.add(info);
        }else
            pageObj = getUserOverviewInfoByNameRoughlyEncoded(keyword.toLowerCase(),page,count);
        return pageObj;
    }

}
