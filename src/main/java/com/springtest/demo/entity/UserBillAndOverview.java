package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.springtest.demo.enums.BillType;
import com.springtest.demo.enums.UserType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName user_bills_and_overview
 */
@TableName(value = "user_bills_and_overview")
@Data
public class UserBillAndOverview implements Serializable {
    public Integer recordId;

    public BillType action;

    public BigDecimal amount;

    public Date time;

    public Integer userId;

    public String remarks;

    public Date refundedTime;

    public Integer id;

    public UserType type;

    public String avatar;

    public String name;

    public String phoneNumber;

    public String email;
}