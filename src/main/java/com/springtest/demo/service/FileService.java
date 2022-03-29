package com.springtest.demo.service;


import com.springtest.demo.Util;
import com.springtest.demo.config.StaticFileConfig;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class FileService {


    public File storePassportImage(MultipartFile file) throws IOException {


        int idx = file.getOriginalFilename().lastIndexOf(".");
        if (idx == -1)
            idx = 0;
        String fileExtension = file.getOriginalFilename().substring(idx);

        String newFileName = Util.generateRandomFileName() + "_" + fileExtension;

        String directoryPath = StaticFileConfig.passportImageDirectory.toString();

        String filePath = Paths.get(directoryPath,newFileName).toString();
        File newFile = new File(filePath);
        file.transferTo(newFile);


        return newFile;
    }




}
