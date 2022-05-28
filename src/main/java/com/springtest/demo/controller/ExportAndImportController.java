package com.springtest.demo.controller;

import com.springtest.demo.dto.ExportOrImport;
import com.springtest.demo.dto.Page;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.enums.UserType;
import com.springtest.demo.service.ExportAndImportService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;

@RestController
public class ExportAndImportController {

    @Autowired
    ExportAndImportService exportAndImportService;


    @GetMapping("/api/exports/{pageNum}")
    public ResponseData<Page<ExportOrImport>> getAllExports(@PathVariable int pageNum,
                                                            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                                                    int pageSize
    ) {

        ResponseData<Page<ExportOrImport>> resp = new ResponseData<>();
        try {
            resp.data = exportAndImportService.getAllExports(pageSize, pageNum);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = ResponseData.unknownError;
            return resp;
        }
    }


    @GetMapping("/api/imports/{pageNum}")
    public ResponseData<Page<ExportOrImport>> getAllImports(@PathVariable int pageNum,
                                                            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                                                                    int pageSize
    ) {

        ResponseData<Page<ExportOrImport>> resp = new ResponseData<>();
        try {
            resp.data = exportAndImportService.getAllImports(pageSize, pageNum);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.status = ResponseData.ERROR;
            resp.errorPrompt = ResponseData.unknownError;
            return resp;
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/export/bank")
    public ResponseData<Prompt> export(@RequestParam UserType userType, @ApiIgnore @RequestAttribute("userId") int userId,
                                       @RequestParam BigDecimal amount, @RequestParam String paymentPassword) {


        ResponseData<Prompt> responseData = new ResponseData<>();

        try {
            responseData.data = exportAndImportService.exportToBank(userType, userId, amount, paymentPassword);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }

    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header"),
    })
    @PostMapping("/api/import/bank")
    public ResponseData<Prompt> importFromBank(@RequestParam UserType userType, @ApiIgnore @RequestAttribute("userId") int userId,
                                               @RequestParam BigDecimal amount) {


        ResponseData<Prompt> responseData = new ResponseData<>();

        try {
            responseData.data = exportAndImportService.importFromBank(userType, userId, amount);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }
    }


    @PostMapping("/api/export")
    public ResponseData<Prompt> export(@RequestAttribute("userId") int userId, @RequestParam BigDecimal amount) {

        ResponseData<Prompt> responseData = new ResponseData<>();

        try {
            responseData.data = exportAndImportService.exportToMerchant(userId, amount);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }
    }


    @PostMapping("/api/import")
    public ResponseData<Prompt> importFromMerchant(@RequestAttribute("userId") int userId, @RequestParam BigDecimal amount) {

        ResponseData<Prompt> responseData = new ResponseData<>();
        try {
            responseData.data = exportAndImportService.exportToUser(userId, amount);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.data = Prompt.unknownError;
            return responseData;
        }
    }
}
