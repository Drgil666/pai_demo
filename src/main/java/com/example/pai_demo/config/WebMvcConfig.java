package com.example.pai_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author GilbertYoung
 * @date 2026/07/22 10:00
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private AuthorizeInterceptor authorizeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor).addPathPatterns("/**");
    }
}
