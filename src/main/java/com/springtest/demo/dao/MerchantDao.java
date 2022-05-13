package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.dto.UserJoinMerchant;
import com.springtest.demo.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MerchantDao extends BaseMapper<Merchant> {


    @Select("select *," +
            "       merchant.frozen_money as merchant_frozen_money," +
            "       merchant.money_amount as merchant_money_amount," +
            "       merchant.state as merchant_state from user right join merchant on user.user_id = merchant.merchant_user_id" +
            "       where merchant.state != 'unverified'  " +
            "       limit ${(pageNumber - 1) * pageSize},#{pageSize}")
    List<UserJoinMerchant> getMerchantAndUser(int pageSize, int pageNumber);

}
