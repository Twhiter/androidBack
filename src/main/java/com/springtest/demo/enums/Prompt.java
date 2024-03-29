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

    invalid_amount("Invalid Amount"),


    accept_user_not_exist("User account doesn't exist"),
    unable_to_delete_verified_user("Unable to delete verified user"),

    accept_merchant_not_exist("Merchant account doesn't exist"),
    unable_to_delete_verified_merchant("Unable to delete verified merchant"),


    refund_pay_id_not_exist("payment doesn't exist"),
    refund_pay_id_already_refunded("payment is already refunded"),
    refund_pay_merchant_not_exist("merchant not exist"),
    refuned_pay_merchant_not_enough_right_refund("You are not allowed to refund the payment"),
    refund_wrong_request_format("Wrong request format"),
    refund_wrong_rsa_keys("Wrong RSA keys"),


    //export to bank
    export_target_not_exist("user/merchant not exist"),
    export_target_frozen("user/merchant is frozen"),
    export_target_unverified("user/merchant is not verified"),
    export_wrong_password("wrong payment password"),
    export_not_enough_balance("No enough balance"),

    //import from bank
    import_target_not_exist("user/merchant not exist"),
    import_target_frozen("user/merchant is frozen"),
    import_target_unverified("user/merchant is not verified"),
    import_not_enough_balance("No enough balance"),


    //import/export between user and merchant
    ei_user_not_exist("user doesn't exist"),
    ei_merchant_not_exist("merchant doesn't exist"),
    ei_user_frozen_state("user account is frozen"),
    ei_merchant_frozen_state("merchant account is frozen"),
    ei_user_unverified("user account is not verified"),
    ei_merchant_unverified("merchant account is not verified"),
    ei_not_enough_balance("Not enough balance");


    @JsonValue
    public String prompt;

    Prompt(String s) {
        prompt = s;
    }
}
