package com.springtest.demo.enums;

public enum RequestBillType {


    income(1),
    outcome(2),
    all(0);

    public final int value;


    RequestBillType(int s) {
        value = s;
    }
}
