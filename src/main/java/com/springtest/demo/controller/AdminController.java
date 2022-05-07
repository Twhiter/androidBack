package com.springtest.demo.controller;

import com.springtest.demo.dto.LoginResp;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.entity.Admin;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.redisEntity.Token;
import com.springtest.demo.service.AdminService;
import com.springtest.demo.service.TokenService;
import com.springtest.demo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {


    @Autowired
    TokenService tokenService;

    @Autowired
    AdminService adminService;



    @PostMapping("/api/token/admin")
    public ResponseData<LoginResp> adminLogin(@RequestBody Admin admin) {


        ResponseData<LoginResp> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {

            var prompt = adminService.login(admin);

            resp.data = new LoginResp();
            resp.data.prompt = prompt;
            resp.data.isOkay = (prompt.equals(Prompt.success));


            //if successful
            if (prompt.equals(Prompt.success)) {

                String tokenStr = Util.generateToken("admin:" + admin.adminAccount);

                resp.data.token = tokenStr;

                Token token = new Token();
                token.id = admin.adminAccount;
                token.token = tokenStr;

                //store token in redis
                tokenService.saveToken(token);
            }

            return resp;
        }catch (Exception e) {

            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
            return resp;
        }
    }

}








