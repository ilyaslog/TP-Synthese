package com.example.tp7.Controller;

import com.example.tp7.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/ChefProjet/**") // Apply to all "/ChefProjet/**" paths
                .excludePathPatterns("/ChefProjet/login", "/ChefProjet/forgot-password"); // Exclude login and password reset pages
    }
}
