package com.springtest.demo.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PayState implements IEnum<String> {

    normal("normal"),
    refunded("refunded");

    @JsonValue
    private String val;

    PayState(String val) {
        this.val = val;
    }


    @Override
    public String getValue() {
        return val;
    }
}
