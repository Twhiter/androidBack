package com.springtest.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Prompt {



    user_account_unverified("Your account is not verified!"),
    user_account_frozen("Your account is frozen. This means you are restricted to do some operations"),
    use_successfully_login(""),
    user_fail_login("Wrong email or password"),


    admin_successfully_login(""),
    admin_fail_login("Wrong account or password"),


    pay_success(""),
    pay_error("error during payment"),
    pay_user_not_found_error("user not found"),
    pay_merchant_not_found_error("merchant not found"),

    pay_user_not_enough_balance("not enough balance"),

    pay_user_to_self_error("can't pay to self merchant account"),

    pay_user_account_frozen("user account is frozen"),
    pay_user_account_unverified("user account is unverified"),

    pay_merchant_account_frozen("merchant account is frozen"),
    pay_merchant_account_unverified("merchant account is unverified");


    @JsonValue
     String prompt;

    Prompt(String s) {
        prompt = s;
    }
}
