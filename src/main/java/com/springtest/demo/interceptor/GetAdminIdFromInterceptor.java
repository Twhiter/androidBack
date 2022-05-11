package com.springtest.demo.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtest.demo.dto.ResponseData;
import com.springtest.demo.service.TokenService;
import com.springtest.demo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GetAdminIdFromInterceptor implements HandlerInterceptor {

    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String tokenStr = request.getHeader("token");
            if (tokenStr == null)
                throw new Exception(ResponseData.authenticationRequired);

            String id = tokenService.verify(tokenStr);
            if (id == null)
                throw new Exception(ResponseData.tokenExpired);


            if (Util.isInteger(id))
                throw new Exception(ResponseData.invalidToken);

            request.setAttribute("adminId", id);

            return true;
        } catch (Exception e) {
            ResponseData responseData = new ResponseData();
            ObjectMapper objectMapper = new ObjectMapper();

            responseData.errorPrompt = e.getMessage();
            responseData.status = ResponseData.ERROR;


            response.getWriter().println(objectMapper.writeValueAsString(responseData));
            response.setStatus(401);
            return false;
        }
    }


}
