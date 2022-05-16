package com.springtest.demo.dto;

import lombok.Data;

@Data
public class RefundRequest {

    public int merchantId;
    public int payId;
    public String signature;
}
