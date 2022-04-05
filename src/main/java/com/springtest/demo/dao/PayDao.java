package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.entity.Pay;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayDao extends BaseMapper<Pay> {

}
