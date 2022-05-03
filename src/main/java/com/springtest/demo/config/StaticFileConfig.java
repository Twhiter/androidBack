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


    public static final Path passportImageDirectory;
    public static final Path avatarImageDirectory;
    public static final Path licenseImageDirectory;

    //set the directory storing passport images to '{project_directory/static/passport_image}'
    static {
        passportImageDirectory = Paths.get(new File("").getAbsolutePath()
                , "static", "passport_image");
        avatarImageDirectory = Paths.get(new File("").getAbsolutePath()
                , "static", "avatar");
        licenseImageDirectory = Paths.get(new File("").getAbsolutePath()
                , "static", "license_image");
    }


    public final static String PASSPORT_IMAGE_WEB_URL_PREFIX = "/passport_image/";
    public final static String AVATAR_IMAGE_WEB_URL_PREFIX = "/avatar/";
    public final static String LICENSE_IMAGE_WEB_URK_PREFIX = "/license_image/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PASSPORT_IMAGE_WEB_URL_PREFIX + "*")
                .addResourceLocations(passportImageDirectory.toUri().toString());

        registry.addResourceHandler(AVATAR_IMAGE_WEB_URL_PREFIX + "*")
                .addResourceLocations(avatarImageDirectory.toUri().toString());

        registry.addResourceHandler(LICENSE_IMAGE_WEB_URK_PREFIX + "*")
                .addResourceLocations(licenseImageDirectory.toUri().toString());

    }


    public static String toWebUrl(String fileName,FileType fileType) {

        return switch (fileType) {
             case passport_image -> PASSPORT_IMAGE_WEB_URL_PREFIX + fileName;
            case avatar -> AVATAR_IMAGE_WEB_URL_PREFIX + fileName;
            case license_image -> LICENSE_IMAGE_WEB_URK_PREFIX + fileName;
        };
    }


    public static String toFileUrl(String webUrl, FileType fileType) {

        return switch (fileType) {
            case passport_image -> webUrl.substring(PASSPORT_IMAGE_WEB_URL_PREFIX.length());
            case avatar -> webUrl.substring(AVATAR_IMAGE_WEB_URL_PREFIX.length());
            case license_image -> webUrl.substring(LICENSE_IMAGE_WEB_URK_PREFIX.length());
        };
    }
}


