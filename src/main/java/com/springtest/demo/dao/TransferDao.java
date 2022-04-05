package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.entity.Transfer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransferDao extends BaseMapper<Transfer> {
}
