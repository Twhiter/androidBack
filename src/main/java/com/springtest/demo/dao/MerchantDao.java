package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantDao extends BaseMapper<Merchant> {

}
