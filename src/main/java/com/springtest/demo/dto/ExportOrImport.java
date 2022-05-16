package com.springtest.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExportOrImport {

    public int id;
    public int from;
    public int to;
    public BigDecimal amount;
    public Date time;
}





