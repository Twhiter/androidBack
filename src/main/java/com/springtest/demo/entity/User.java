package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springtest.demo.enums.State;
import lombok.Data;

/**
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {


    @TableId(type = IdType.AUTO)
    public Integer userId;


    public String phoneNumber;


    public String firstName;


    public String lastName;

    @JsonIgnore
    public String passportNumber;


    public String country;


    public String email;
    public String passportPhoto;


    @JsonIgnore
    public String password;
    @JsonIgnore
    public String paymentPassword;


    public State state;
    public BigDecimal moneyAmount;
    public BigDecimal frozenMoney;
    public String avatar;

}