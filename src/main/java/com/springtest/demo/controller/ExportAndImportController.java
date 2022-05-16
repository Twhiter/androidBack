package com.springtest.demo.controller;

import com.springtest.demo.dto.ExportOrImport;
import com.springtest.demo.dto.Page;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.service.ExportAndImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
