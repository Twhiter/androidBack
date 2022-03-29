package com.springtest.demo.config;

import com.springtest.demo.enums.FileType;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticFileConfig implements WebMvcConfigurer {


    public static Path passportImageDirectory;

    //set the directory storing passport images to '{project_directory/static/passport_image}'
    static {
        passportImageDirectory = Paths.get(new File("").getAbsolutePath()
                ,"static","passport_image");
    }


    public final static String PASSPORT_IMAGE_WEB_URL_PREFIX = "/passport_image/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PASSPORT_IMAGE_WEB_URL_PREFIX + "*")
                .addResourceLocations(passportImageDirectory.toUri().toString());
    }


    public static String toWebUrl(String fileName,FileType fileType) {


        return switch (fileType) {
             case passport_image -> PASSPORT_IMAGE_WEB_URL_PREFIX + fileName;
            case avatar -> null;
        };
    }


    public static String toFileUrl(String webUrl, FileType fileType) {

        return switch (fileType) {
            case passport_image -> webUrl.substring(PASSPORT_IMAGE_WEB_URL_PREFIX.length());
            case avatar -> null;
        };
    }
}


