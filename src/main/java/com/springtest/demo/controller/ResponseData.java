package com.springtest.demo.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ResponseData<T> {

    public static int OK = 200;
    public static int ERROR = -1;


    public String errorPrompt = null;
    public Integer status = OK;
    public T data = null;
}


