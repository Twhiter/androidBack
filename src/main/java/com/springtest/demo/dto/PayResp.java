package com.springtest.demo.dto;

import com.springtest.demo.enums.Prompt;
import lombok.Data;

@Data

public class PayResp {
    public Prompt prompt;
    public PayOverview payOverview;
}
