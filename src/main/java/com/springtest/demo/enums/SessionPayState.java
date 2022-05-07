package com.springtest.demo.enums;

public enum SessionPayState {

    un_paid(1),
    paid_waiting_for_verified(2),
    already_paid(3);


    private int order;

    SessionPayState(int order) {
        this.order = order;
    }
}
