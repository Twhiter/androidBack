package com.springtest.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName merchant_rsa_public_key
 */
@TableName(value = "merchant_rsa_public_key")
@Data
public class MerchantRsaPublicKey implements Serializable {
    /**
     *
     */
    @TableId
    public Integer merchantId;

    /**
     *
     */
    public Integer publicKey;
}