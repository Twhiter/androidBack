package com.springtest.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {


    merchant("merchant"),
    user("user"),
    admin("admin");

    @JsonValue
    public String value;

    UserType(String val) {
        this.value = val;
    }
}
