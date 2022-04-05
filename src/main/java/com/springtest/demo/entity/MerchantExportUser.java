package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName merchant_export_user
 */
@TableName(value ="merchant_export_user")
@Data
public class MerchantExportUser {

    @TableId(type = IdType.AUTO)
    public Integer exportId;


    public Integer merchantId;


    public BigDecimal amount;


    public Date time;
}