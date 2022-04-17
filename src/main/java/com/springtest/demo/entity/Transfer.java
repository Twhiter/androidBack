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
 * @TableName transfer
 */
@TableName(value ="transfer")
@Data
public class Transfer implements Serializable {

    @TableId(type = IdType.AUTO)
    public Integer transferId;
    public Integer sourceUserId;
    public Integer targetUserId;
    public Date time;
    public BigDecimal amount;
    public String remarks;
}