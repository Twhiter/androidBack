package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName refund
 */
@TableName(value ="refund")
@Data
public class Refund implements Serializable {

    @TableId(type = IdType.AUTO)
    public Integer refundId;


    public Integer payId;


    public Date time;
}