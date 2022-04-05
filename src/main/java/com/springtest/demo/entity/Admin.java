package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName(value ="admin")
@Data
public class Admin{
    @TableId
    public String adminAccount;


    public String password;

}