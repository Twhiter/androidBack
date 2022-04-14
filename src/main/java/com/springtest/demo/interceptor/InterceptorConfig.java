package com.springtest.demo.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig  implements WebMvcConfigurer {

    @Autowired
    GetUserIdFromTokenInterceptor getUserIdFromTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserIdFromTokenInterceptor)
                .addPathPatterns("/api/user/self")
                .addPathPatterns("/api/merchant/self")
                .addPathPatterns("/api/pay");
    }
}
