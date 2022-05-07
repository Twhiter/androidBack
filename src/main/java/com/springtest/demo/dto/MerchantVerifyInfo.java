package com.springtest.demo.dto;

import lombok.Data;

@Data
public class MerchantVerifyInfo {
    public int sessionId;
    public String signature;
}
