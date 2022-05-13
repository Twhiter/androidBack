package com.springtest.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ModifyFrozenAmount {


    public int id;
    public BigDecimal amount;

    public String reasons;


}
