package com.springtest.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Prompt {


    success(""),
    unknownError("Unknown Error,Please try again"),

    user_account_unverified("Your account is not verified!"),
    user_account_frozen("Your account is frozen. This means you are restricted to do some operations"),
    user_fail_login("Wrong email or password"),


    admin_fail_login("Wrong account or password"),

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


    pay_session_id_error("Invalid QR Code"),
    pay_time_out("Payment Timeout"),
    multiple_user_pay_error("only one user can pay at one time"),


    session_pay_request_format_error("Wrong request format"),

    pay_verify_request_format_error("Wrong request format"),
    pay_merchant_key_not_exist("You haven't applied for keys"),
    pay_verify_wrong_signature("Wrong signature"),


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

    payment_password_not_correct("payment password not correct"),

    merchant_phoneNumber_already_exist("The company/organization phone already registered"),
    merchant_license_number_already_exist("The compant/organization license number already registered"),
    duplicate_register("You have already registered a merchant account!"),


    freeze_user_not_exist_error("user not exist"),
    freeze_user_account_not_verified("User Account is not verified!"),

    unfreeze_user_not_exist_error("user not exist"),
    unfreeze_user_account_not_verified("User Account is not verified!"),


    freeze_merchant_not_exist("Merchant not exist"),
    freeze_merchant_account_not_verified("Merchant Account is not verified!"),

    unfreeze_merchant_not_exist_error("merchant not exist"),
    unfreeze_merchant_account_not_verified("Merchant Account is not verified!"),


    freeze_user_balance_user_not_exist("user not exist"),
    freeze_user_balance_user_not_enough_balance("user doesn't own enough balance"),
    freeze_user_balance_user_account_not_verified("User account is not verified"),


    unfreeze_user_balance_frozen_amount_not_enough("exceed frozen amount"),


    freeze_merchant_balance_merchant_not_exist("merchant not exist"),
    freeze_merchant_balance_merchant_not_enough_balance("merchant doesn't own enough balance"),
    freeze_merchant_balance_merchant_account_not_verified("merchant account is not verified"),
    unfreeze_merchant_balance_frozen_amount_not_enough("exceed frozen amount"),

    invalid_amount("Invalid Amount");


    @JsonValue
    public String prompt;

    Prompt(String s) {
        prompt = s;
    }
}
