package com.springtest.demo.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum State implements IEnum<String> {

    frozen("frozen"),
    unverified("unverified"),
    normal("normal");

    @JsonValue
    private String state;

    State(String val) {
        this.state = val;
    }

    @Override
    public String getValue() {
        return state;
    }
}
