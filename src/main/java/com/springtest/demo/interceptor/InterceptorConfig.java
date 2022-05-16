package com.springtest.demo.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    GetUserIdFromTokenInterceptor getUserIdFromTokenInterceptor;

    @Autowired
    GetAdminIdFromInterceptor getAdminIdFromInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserIdFromTokenInterceptor)
                .addPathPatterns("/api/user/self")
                .addPathPatterns("/api/merchant/self")
                .addPathPatterns("/api/pay")
                .addPathPatterns("/api/transfer")
                .addPathPatterns("/api/bills/merchant", "/api/bills/user")
                .addPathPatterns("/api/merchant")
                .addPathPatterns("/api/payWithConfirm")
                .addPathPatterns("/api/payment/state");


        //registry.addInterceptor(getAdminIdFromInterceptor);


    }
}
