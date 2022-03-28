package com.springtest.demo.controller;


import com.springtest.demo.service.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
public class VerifyController {

    @Autowired
    ServiceUtil serviceUtil;

    @PostMapping("/api/verifyCode")
    public ResponseData sendVerifyCode(@RequestBody Map<String,String> requestData) {

        ResponseData responseData = new ResponseData<>();

        try {
            switch (requestData.get("type")) {
                case "phone" -> serviceUtil.sendPhoneVerifyCode(requestData.get("target"));
                case "email" -> serviceUtil.sendEmailVerifyCode(requestData.get("target"));
                default -> {
                    responseData.errorPrompt = "Error Request Format";
                    responseData.status = ResponseData.ERROR;
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            responseData.status = ResponseData.ERROR;
            responseData.errorPrompt = ("Error! Email address error! Please Check");
        }catch (Exception e) {
            e.printStackTrace();
            responseData.status = ResponseData.ERROR;
            responseData.errorPrompt = ("Error!");
        }

        return responseData;
    }

    @GetMapping("/api/verifyCode")
    public ResponseData<Boolean> checkVerifyCode(@RequestParam(name = "type")String type
            ,@RequestParam(name = "target")String target,@RequestParam(name = "code") String code) {

        ResponseData<Boolean> responseData = new ResponseData<>();
        responseData.data = true;

        try {
            switch (type) {
                case "phone" -> responseData.data = serviceUtil.checkPhoneVerifyCode(target,code);
                case "email" -> responseData.data = serviceUtil.checkEmailVerifyCode(target,code);
                default ->  {
                    responseData.status = ResponseData.ERROR;
                    responseData.errorPrompt = ("Error Request Format");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            responseData.status = ResponseData.ERROR;
            responseData.errorPrompt = "ERROR!";
        }

        return responseData;
    }
}
