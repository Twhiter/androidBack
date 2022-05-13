package com.springtest.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springtest.demo.enums.State;
import lombok.Data;

@Data
public class ModifyStateRequest {
    public int id;
    public State state;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String reasons;
}
