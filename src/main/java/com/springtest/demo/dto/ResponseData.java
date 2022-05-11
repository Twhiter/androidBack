package com.springtest.demo.dto;


import lombok.Data;

@Data
public class ResponseData<T> {

    public static int OK = 200;
    public static int ERROR = -1;
    public static String invalidToken = "Invalid Token";
    public static String authenticationRequired = "Authentication Required";
    public static String tokenExpired = "Your token have expired, please relogin";
    public static String unknownError = "Unknown Error";


    public String errorPrompt = null;
    public Integer status = OK;
    public T data = null;
}


