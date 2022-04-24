package com.springtest.demo.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BillType implements IEnum<String> {

    transfer_in("Transfer In"),
    transfer_out("Transfer Out"),
    pay("Pay"),
    pay_with_refunded("Pay(Refunded)"),
    import_from_user("Import From Individual Account"),
    import_from_merchant("Import From Merchant Account"),
    export_to_user("Export To Individual Account"),
    export_to_merchant("Export To Merchant Account");

    @JsonValue
    private final String value;

    BillType(String s) {
        value = s;
    }

    @Override
    public String getValue() {
        return value;
    }
}
