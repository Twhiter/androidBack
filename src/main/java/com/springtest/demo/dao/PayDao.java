package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.dto.PaymentWithRefund;
import com.springtest.demo.entity.Pay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PayDao extends BaseMapper<Pay> {


    @Select("select *,refund.time as refunded_time from pay left join refund on pay.pay_id = refund.pay_id " +
            " limit ${pageSize * (pageNum - 1)},#{pageSize}")
    List<PaymentWithRefund> getAllPays(int pageNum, int pageSize);


}
