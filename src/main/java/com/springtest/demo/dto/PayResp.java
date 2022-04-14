package com.springtest.demo.dto;

import com.springtest.demo.enums.Prompt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class PayResp {
    public Prompt prompt;
    public PayOverview payOverview;
}
