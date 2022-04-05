package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.springtest.demo.enums.State;
import lombok.Data;

/**
 * 
 * @TableName merchant
 */
@TableName(value ="merchant")
@Data
public class Merchant {

    @TableId(type = IdType.AUTO)
    public Integer merchantId;


    public Integer merchantUserId;


    public String merchantName;


    public String merchantLicense;


    public String merchantLicensePhoto;


    public String merchantPhoneNumber;


    public String merchantLogo;


    public String merchantEmail;


    public BigDecimal frozenMoney;


    public BigDecimal moneyAmount;


    public State state;

}