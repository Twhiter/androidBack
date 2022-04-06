package com.springtest.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Prompt {



    user_account_unverified("Your account is not verified!"),
    user_account_frozen("Your account is frozen. This means you are restricted to do some operations"),
    use_successfully_login(""),
    user_fail_login("Wrong email or password"),


    admin_successfully_login(""),
    admin_fail_login("Wrong account or password");


    @JsonValue
     String prompt;

    Prompt(String s) {
        prompt = s;
    }
}
