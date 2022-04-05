package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.springtest.demo.enums.PayState;
import lombok.Data;

/**
 * 
 * @TableName pay
 */
@TableName(value ="pay")
@Data
public class Pay implements Serializable {

    @TableId(type = IdType.AUTO)
    public Integer payId;


    public Integer sourceUserId;


    public Integer targetMerchantId;


    public Date time;


    public PayState state;


    public BigDecimal amount;


    public BigDecimal fee;

    @TableField(exist = false)
    public static final long serialVersionUID = 1L;
}