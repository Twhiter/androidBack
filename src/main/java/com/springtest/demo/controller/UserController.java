package com.springtest.demo.controller;


import com.springtest.demo.config.StaticFileConfig;
import com.springtest.demo.dto.*;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.FileType;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.redisEntity.Token;
import com.springtest.demo.service.FileService;
import com.springtest.demo.service.ServiceUtil;
import com.springtest.demo.service.TokenService;
import com.springtest.demo.service.UserService;
import com.springtest.demo.util.Util;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Autowired
    TokenService tokenService;

    @Autowired
    ServiceUtil serviceUtill;


    @PostMapping("/api/user")
    public ResponseData userRegister(@RequestParam("phoneNumber") String phoneNumber
            , @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName
            , @RequestParam("passportNumber") String passportNumber, @RequestParam("country") String country
            , @RequestParam("email") String email, @RequestParam("passportPhoto") MultipartFile passportPhoto
            , @RequestParam("password") String password, @RequestParam("paymentPassword") String paymentPassword) {


        ResponseData resp = new ResponseData();
        File f = null;

        try {
            try {
                f = fileService.storePassportImage(passportPhoto);
            } catch (IOException e) {
                e.printStackTrace();
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
        } catch (Exception e) {
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
    public ResponseData<LoginResp> userLogin(@RequestBody Map<String, String> requestData) {

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

            var userAndPrompt = userService.login(phone, password);
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
        } catch (Exception e) {

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
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @GetMapping("/api/user/self")
    public ResponseData<User> getUser(@ApiIgnore @RequestAttribute("userId") int userId) {
        return getUserInfo(userId);
    }


    @GetMapping("/api/user/overview/{userId}")
    public ResponseData<OverviewInfo> getUserOverview(@PathVariable int userId) {
        ResponseData<OverviewInfo> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = userService.getUserOverview(userId);
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }


    @GetMapping("/api/user/search")
    public ResponseData<Page<OverviewInfo>> searchUsers(@RequestParam("keyword") String keyword,
                                                        @RequestParam("page") int page,
                                                        @RequestParam("pageCount") int pageCount) {

        ResponseData<Page<OverviewInfo>> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = userService.searchUser(keyword, page, pageCount);
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;

    }


    @GetMapping("/api/users/{pageNumber}")
    public ResponseData<Page<UserAndMerchant>> getUsers(@PathVariable int pageNumber,
                                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                                                int pageSize) {


        ResponseData<Page<UserAndMerchant>> resp = new ResponseData<>();
        resp.status = ResponseData.OK;

        try {
            resp.data = userService.getUsers(pageNumber, pageSize);
        } catch (Exception e) {
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = "Error";
        }
        return resp;
    }


    @PutMapping("/api/user/state")
    public ResponseData<Prompt> updateUserState(@RequestBody ModifyStateRequest obj) {

        ResponseData<Prompt> resp = new ResponseData<>();

        try {
            User user = userService.getUserByAdmin(obj.id);

            switch (obj.state) {
                case frozen -> {
                    resp.data = userService.freezeUser(obj.id);
                    if (resp.data != Prompt.success)
                        return resp;

                    new Thread(() -> {
                        try {
                            serviceUtill.sendFrozenMessage(user.email, user.phoneNumber, obj.reasons);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }).start();

                }
                case normal -> resp.data = userService.unfreezeUser(obj.id);
                case unverified -> throw new Exception();
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.data = Prompt.unknownError;
        }

        return resp;
    }


    @PutMapping("/api/user/frozenAmount")
    public ResponseData<Prompt> updateUserFrozenAmount(@RequestBody ModifyFrozenAmount obj) {

        ResponseData<Prompt> resp = new ResponseData<>();

        try {

            if (obj.amount.compareTo(BigDecimal.ZERO) > 0)
                resp.data = userService.freezeUserBalance(obj.id, obj.amount);
            else
                resp.data = userService.unfreezeUserBalance(obj.id, obj.amount.multiply(BigDecimal.valueOf(-1)));

            if (resp.data != Prompt.success)
                return resp;


            try {
                User user = userService.getUserByAdmin(obj.id);
                new Thread(() -> {

                    try {
                        serviceUtill.sendFrozenBalanceMessage(user.email, user.phoneNumber, obj.reasons, obj.amount);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
            resp.data = Prompt.unknownError;
        }

        return resp;
    }


    @GetMapping("/api/users/unverified/{pageNum}")
    public ResponseData<Page<User>> getUnverifiedUsers(@RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                                               int pageSize,
                                                       @PathVariable int pageNum
    ) {

        ResponseData<Page<User>> resp = new ResponseData<>();
        try {
            resp.data = userService.getUnverifiedUsers(pageSize, pageNum);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.status = ResponseData.ERROR;
            return resp;
        }
    }


    @PutMapping("/api/user/unverified")
    public ResponseData<Prompt> acceptUser(@RequestBody int userId) {

        ResponseData<Prompt> resp = new ResponseData<>();

        try {
            resp.data = userService.acceptUser(userId);
            if (resp.data != Prompt.success)
                return resp;
            else {

                User user = userService.getUserByAdmin(userId);

                new Thread(() -> {
                    try {
                        serviceUtill.sendAcceptMessage(user.email, user.phoneNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                return resp;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.data = Prompt.unknownError;
            return resp;
        }

    }


    @PostMapping("/api/user/reject")
    public ResponseData<Prompt> rejectUser(@RequestBody RejectRequest obj) {

        ResponseData<Prompt> responseData = new ResponseData<>();


        try {

            User user = userService.getUserByAdmin(obj.id);

            responseData.data = userService.deleteUser(obj.id);
            if (responseData.data != Prompt.success)
                return responseData;

            new Thread(() -> {

                try {
                    serviceUtill.sendRejectMessage(user.email, user.phoneNumber, obj.reasons);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

            return responseData;

        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }

    }


}
