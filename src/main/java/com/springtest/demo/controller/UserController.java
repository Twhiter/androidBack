package com.springtest.demo.controller;


import com.springtest.demo.config.StaticFileConfig;
import com.springtest.demo.dto.LoginResp;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.dto.Page;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.FileType;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.redisEntity.Token;
import com.springtest.demo.service.FileService;
import com.springtest.demo.service.TokenService;
import com.springtest.demo.service.UserService;
import com.springtest.demo.util.Util;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Autowired
    TokenService tokenService;




    @PostMapping("/api/user")
    public ResponseData userRegister(@RequestParam("phoneNumber") String phoneNumber
            , @RequestParam("firstName")String firstName, @RequestParam("lastName")String lastName
            , @RequestParam("passportNumber")String passportNumber, @RequestParam("country")String country
            , @RequestParam("email")String email, @RequestParam("passportPhoto")MultipartFile passportPhoto
            , @RequestParam("password") String password, @RequestParam("paymentPassword")String paymentPassword) {


        ResponseData resp = new ResponseData();
        File f = null;

            try {
                try {
                    f = fileService.storePassportImage(passportPhoto);
                } catch (IOException e) {
                    resp.status = ResponseData.ERROR;
                    resp.errorPrompt = "Error! Please Try Again!";
                    return resp;
                }

                User user = new User();
                user.setCountry(country);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPassportNumber(passportNumber);
                user.setPassportPhoto(StaticFileConfig.toWebUrl(f.getName(), FileType.passport_image));
                user.setPassword(password);
                user.setPaymentPassword(paymentPassword);
                user.setPhoneNumber(phoneNumber);


                userService.register(user);
            }catch (Exception e) {
                e.printStackTrace();
                resp.status = ResponseData.ERROR;
                resp.errorPrompt = "Error!";

                if (f != null)
                    f.delete();

                return resp;
            }
            return resp;
    }


    @PostMapping("/api/token/user")
    public ResponseData<LoginResp> userLogin(@RequestBody Map<String,String> requestData) {

        ResponseData<LoginResp> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {

            String phone = requestData.get("phone");
            String password = requestData.get("password");

            if (phone == null || password == null) {
                resp.errorPrompt = "Error request format";
                resp.status = ResponseData.ERROR;
                return resp;
            }

            var userAndPrompt = userService.login(phone,password);
            User user = (User) userAndPrompt.get("user");
            Prompt prompt = (Prompt) userAndPrompt.get("prompt");


           resp.data = new LoginResp();
           resp.data.prompt = prompt;
            resp.data.isOkay = (prompt.equals(Prompt.success));

           //if successful
            if (prompt.equals(Prompt.success)) {

                String tokenStr = Util.generateToken("user:" + user.userId);
                resp.data.token = tokenStr;

                Token token = new Token();
                token.id = user.userId.toString();
                token.token = tokenStr;

                tokenService.saveToken(token);
            }
           return resp;
        }catch (Exception e) {

            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
            return resp;
        }
    }

    @GetMapping("/api/user/{userid}")
    public ResponseData<User> getUserInfo(@PathVariable(name = "userid") int id) {

        ResponseData<User> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = userService.getUserByUser(id);
        }catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @GetMapping("/api/user/self")
    public ResponseData<User> getUser(@ApiIgnore  @RequestAttribute("userId") int userId) {
        return getUserInfo(userId);
    }


    @GetMapping("/api/user/overview/{userId}")
    public ResponseData<OverviewInfo> getUserOverview(@PathVariable int userId) {
        ResponseData<OverviewInfo> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = userService.getUserOverview(userId);
        }catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }


    @GetMapping("/api/user/search")
    public ResponseData<Page<OverviewInfo>> searchUsers(@RequestParam("keyword") String keyword,
                                          @RequestParam("page") int page,
                                          @RequestParam("pageCount")int pageCount) {

        ResponseData<Page<OverviewInfo>> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = userService.searchUser(keyword,page,pageCount);
        }catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;

    }
}
