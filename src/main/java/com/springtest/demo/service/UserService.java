package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.util.Util;
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

    public List<OverviewInfo> getUserOverviewInfosByEmail(String email) {

        List<User> users = userDao.selectList(new QueryWrapper<User>().eq("email",email));
        return users.stream().map(OverviewInfo::fromUser).toList();
    }


    public List<OverviewInfo> getUserOverviewInfoByNameRoughly(String roughName,int page,int count) {

        List<User>users = userDao.selectList(new QueryWrapper<User>()
                .apply("concat(lower(first_name),' ',lower(last_name)) like {0}"
                        ,"%" + roughName.toLowerCase() + "%")
                .last(String.format("limit %d,%d",(page - 1) * count,count))
        );

        return users.stream().map(OverviewInfo::fromUser).toList();
    }


    public OverviewInfo getUserOverviewInfoByPhoneEncoded(String phoneNumber) {
        var overviewInfo =  getUserOverviewInfoByPhone(phoneNumber);
        overviewInfo.email = Util.encodeEmail(overviewInfo.email);
        return overviewInfo;
    }

    public List<OverviewInfo> getUserOverviewInfosByEmailEncoded(String email) {

        var overviewInfos = getUserOverviewInfosByEmail(email);
        return overviewInfos.stream()
                .peek(overviewInfo -> overviewInfo.phoneNumber = Util.encodePhone(overviewInfo.phoneNumber))
                .toList();
    }


    public List<OverviewInfo> getUserOverviewInfoByNameRoughlyEncoded(String roughName,int page,int count) {
        var overviewInfos = getUserOverviewInfoByNameRoughly(roughName,page,count);

        return overviewInfos.stream()
                .peek(overviewInfo -> {
                    overviewInfo.phoneNumber = Util.encodePhone(overviewInfo.phoneNumber);
                    overviewInfo.email = Util.encodeEmail(overviewInfo.email);
                })
                .toList();
    }


    public List<OverviewInfo> searchUser(String keyword,int page,int count) {

        List<OverviewInfo> infos = new ArrayList<>();

        if (keyword.matches(Util.EMAIL_PATTERN)) {
            infos = getUserOverviewInfosByEmailEncoded(keyword.toLowerCase());
        }else if (keyword.matches("\\+\\d+")) {
            var info = this.getUserOverviewInfoByPhoneEncoded(keyword.toLowerCase());
            if (info != null)
                infos.add(info);
        }else
            infos = getUserOverviewInfoByNameRoughlyEncoded(keyword.toLowerCase(),page,count);
        return infos;
    }

}
