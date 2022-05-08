package com.springtest.demo.dto;

import com.springtest.demo.enums.Prompt;
import lombok.Data;

@Data
public class SessionPayResp {

    public Prompt prompt;
    public int sessionId;
}
