package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserDao extends BaseMapper<User> {

}
