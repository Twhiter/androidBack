package com.springtest.demo.controller;



import com.springtest.demo.config.StaticFileConfig;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.FileType;
import com.springtest.demo.service.FileService;
import com.springtest.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;




    @PostMapping("/api/user")
    public ResponseData userRegister(@RequestParam("phoneNumber") String phoneNumber
            , @RequestParam("firstName")String firstName, @RequestParam("lastName")String lastName
            , @RequestParam("passportNumber")String passportNumber, @RequestParam("country")String country
            , @RequestParam("email")String email, @RequestParam("passportPhoto")MultipartFile passportPhoto
            ,@RequestParam("password") String password,@RequestParam("paymentPassword")String paymentPassword) {


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
}
