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
 * @TableName user_export_merchant
 */
@TableName(value ="user_export_merchant")
@Data
public class UserExportMerchant implements Serializable {

    @TableId(type = IdType.AUTO)
    public Integer exportId;


    public Integer userId;


    public BigDecimal amount;


    public Date time;

}