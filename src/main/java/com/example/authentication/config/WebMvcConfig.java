package com.example.authentication.config;


import com.example.authentication.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebMvcConfig(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/user");
//    }

}
