package com.springtest.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springtest.demo.enums.Prompt;
import lombok.Data;

@Data
public class LoginResp {


    public String token = null;
    @JsonProperty("isOkay")
    public boolean isOkay = false;
    public Prompt prompt;
}
