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
    pay_merchant_account_unverified("merchant account is unverified"),
    pay_amount_invalid_error("amount must be larger than 0"),



    transfer_source_not_exist("Transfer source account does not exist"),
    transfer_target_not_exist("Transfer target account does not exist"),

    transfer_source_account_frozen("Your account is frozen"),
    transfer_target_account_frozen("Target account is frozen"),

    transfer_source_account_unverified("Your account is unverified"),
    transfer_target_account_unverified("Target account is unverified"),

    transfer_to_self_error("Can't transfer to yourself"),

    transfer_not_enough_balance("Not enough balance"),
    transfer_error("Error during transfer"),
    transfer_amount_invalid_error("amount must be larger than 0"),
    transfer_success("");



    @JsonValue
    public String prompt;

    Prompt(String s) {
        prompt = s;
    }
}
