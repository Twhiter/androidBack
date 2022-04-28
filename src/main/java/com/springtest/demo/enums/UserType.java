package com.springtest.demo.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType implements IEnum<String> {


    merchant("merchant"),
    user("user"),
    admin("admin");

    @JsonValue
    public String value;

    UserType(String val) {
        this.value = val;
    }

    @Override
    public String getValue() {
        return value;
    }
}
