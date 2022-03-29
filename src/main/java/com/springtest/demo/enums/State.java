package com.springtest.demo.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum State implements IEnum<String> {

    FROZEN("frozen"),
    Unverified("unverified"),
    normal("normal");


    private String state;

    State(String val) {
        this.state = val;
    }

    @Override
    public String getValue() {
        return state;
    }
}
