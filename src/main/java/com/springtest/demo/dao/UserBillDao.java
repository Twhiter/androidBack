package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.entity.UserBill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBillDao extends BaseMapper<UserBill> {
}
