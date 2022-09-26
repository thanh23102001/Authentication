package com.example.authentication.interceptor;

import com.example.authentication.error.NoBearerTokenError;
import com.example.authentication.service.AuthService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor {
    private final AuthService authService;


    public AuthorizationInterceptor(AuthService authService) {
        this.authService =  authService;
    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
//            throw new NoBearerTokenError();
//
//        request.setAttribute("user ", authService.get);
//
//    }
}
