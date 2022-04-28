package com.springtest.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springtest.demo.entity.MerchantBillAndOverview;
import com.springtest.demo.entity.UserBillAndOverview;
import com.springtest.demo.enums.BillType;
import com.springtest.demo.enums.UserType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Data
public class BillRecord {

    @JsonProperty("overviewInfo")
    public OverviewInfo overviewInfo;

    @JsonProperty("billType")
    public BillType billType;

    @JsonProperty("recordId")
    public int recordId;

    @JsonProperty("amount")
    public BigDecimal amount;

    @JsonProperty("date")
    public Date date;

    @JsonProperty("extraData")
    public HashMap<String, Object> extraData;


    public static BillRecord fromMerchantBillAndOverview(MerchantBillAndOverview ins) {

        return getBillRecord(ins.id, ins.type, ins.avatar, ins.name, ins.phoneNumber, ins.email, ins.action,
                ins.recordId, ins.amount, ins.time, ins.refundedTime, ins.remarks);
    }

    public static BillRecord fromUserBillAndOverview(UserBillAndOverview ins) {

        return getBillRecord(ins.id, ins.type, ins.avatar, ins.name, ins.phoneNumber, ins.email, ins.action,
                ins.recordId, ins.amount, ins.time, ins.refundedTime, ins.remarks);


    }

    private static BillRecord getBillRecord(Integer id, UserType type, String avatar, String name, String phoneNumber,
                                            String email, BillType action, Integer recordId, BigDecimal amount,
                                            Date time, Date refundedTime, String remarks) {
        BillRecord obj = new BillRecord();

        obj.overviewInfo = new OverviewInfo(id, type, avatar, name, phoneNumber, email);
        obj.billType = action;
        obj.recordId = recordId;
        obj.amount = amount;
        obj.date = time;
        obj.extraData = new HashMap<>();

        if (refundedTime != null)
            obj.extraData.put("refundedTime", refundedTime);
        if (remarks != null)
            obj.extraData.put("remarks", remarks);

        return obj;
    }


}
