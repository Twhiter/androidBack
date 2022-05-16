package com.springtest.demo.dto;

import com.springtest.demo.enums.Prompt;
import lombok.Data;

@Data
public class VerifyPayResp {

    public int payId;
    public Prompt prompt;

}
