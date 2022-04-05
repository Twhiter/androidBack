package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.entity.Refund;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefundDao extends BaseMapper<Refund> {
}
