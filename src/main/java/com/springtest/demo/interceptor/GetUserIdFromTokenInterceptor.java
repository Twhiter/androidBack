package com.springtest.demo.interceptor;

import com.springtest.demo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GetUserIdFromTokenInterceptor implements HandlerInterceptor {


    @Autowired
    TokenService tokenService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

       String tokenStr = request.getHeader("token");
       if (tokenStr == null)
           return false;

       String id = tokenService.verify(tokenStr);
       if (id == null)
           return false;

       try {
           int user_id = Integer.parseInt(id);
           request.setAttribute("userId",user_id);
           return true;
       }catch (Exception e) {
           return false;
       }

    }
}
