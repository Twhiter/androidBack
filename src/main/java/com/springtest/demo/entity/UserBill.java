package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.springtest.demo.enums.BillType;
import com.springtest.demo.enums.UserType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName user_bills
 */
@TableName(value = "user_bills")
@Data
public class UserBill implements Serializable {
    public Integer recordId;

    public BillType action;

    public BigDecimal amount;

    public Date time;

    public Integer userId;

    public Integer targetId;

    public UserType targetType;

    public String remarks;

    public Date refundedTime;
}