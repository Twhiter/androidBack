package com.springtest.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class RejectRequest {

    public int id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String reasons;


}
