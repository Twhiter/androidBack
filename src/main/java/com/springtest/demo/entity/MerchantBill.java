package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.springtest.demo.enums.BillType;
import com.springtest.demo.enums.UserType;
import lombok.Data;

/**
 * 
 * @TableName merchant_bills
 */
@TableName(value ="merchant_bills")
@Data
public class MerchantBill implements Serializable {
    public Integer recordId;

    public BillType action;

    public BigDecimal amount;

    public Date time;

    public Integer merchantId;

    public Integer targetId;

    public UserType targetType;

    public Date refundedTime;

    public String remarks;
}