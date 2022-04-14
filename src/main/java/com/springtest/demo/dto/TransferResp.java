package com.springtest.demo.dto;


import com.springtest.demo.entity.Transfer;
import com.springtest.demo.enums.Prompt;
import lombok.Data;

@Data
public class TransferResp {


    public Prompt prompt;
    public Transfer transfer;

}
