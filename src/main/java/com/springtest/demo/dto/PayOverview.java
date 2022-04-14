package com.springtest.demo.dto;


import com.springtest.demo.entity.Pay;
import com.springtest.demo.enums.PayState;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayOverview {

    public Integer payId;
    public Integer userId;
    public Integer merchantId;
    public BigDecimal amount;
    public Date time;
    public PayState state;



    public static PayOverview fromPay(Pay pay) {
        if (pay == null)
            return null;
        PayOverview payOverview = new PayOverview();
        payOverview.payId = pay.payId;
        payOverview.userId = pay.sourceUserId;
        payOverview.merchantId = pay.targetMerchantId;
        payOverview.amount = pay.amount;
        payOverview.time = pay.time;
        payOverview.state = pay.state;

        return payOverview;
    }
}
