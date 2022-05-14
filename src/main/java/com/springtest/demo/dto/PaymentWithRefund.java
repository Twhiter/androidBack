package com.springtest.demo.dto;

import com.springtest.demo.entity.Pay;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentWithRefund extends Pay {
    public Date refundedTime;
}
