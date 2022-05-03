package com.springtest.demo.service;


import com.springtest.demo.config.StaticFileConfig;
import com.springtest.demo.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class FileService {


    public File storePassportImage(MultipartFile file) throws IOException {
        return storeFile(file, StaticFileConfig.passportImageDirectory.toString());
    }

    public File storeLicenseImage(MultipartFile file) throws IOException {
        return storeFile(file, StaticFileConfig.licenseImageDirectory.toString());
    }


    static private File storeFile(MultipartFile file, String directoryPath) throws IOException {

        int idx = file.getOriginalFilename().lastIndexOf(".");

        String fileExtension = file.getOriginalFilename().substring(idx + 1);

        String newFileName = Util.generateRandomFileName() + "_" + fileExtension;


        String filePath = Paths.get(directoryPath, newFileName).toString();
        File newFile = new File(filePath);
        file.transferTo(newFile);

        return newFile;
    }


}
