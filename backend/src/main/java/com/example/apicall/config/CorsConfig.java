package com.example.apicall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import com.example.logintegrator.web.interceptor.LoggingInterceptor;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${logging.interceptor.enabled:false}")
    private boolean loggingInterceptorEnabled;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (loggingInterceptorEnabled) {
            registry.addInterceptor(new LoggingInterceptor())
                    .addPathPatterns("/**")
                    .excludePathPatterns("/error")
                    .excludePathPatterns("/logs/import"); // 排除日志导入接口
        }
    }
}