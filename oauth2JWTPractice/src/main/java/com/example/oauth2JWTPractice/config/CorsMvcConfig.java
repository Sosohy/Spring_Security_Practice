package com.example.oauth2JWTPractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {  // 컨트롤러를 타고 응답되는 경우 WebMvcConfigurer 설정을 통해 진행

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .exposedHeaders("Set-Cookie", "USER_ROLE", "Authorization")
                .allowedOrigins("http://localhost:8080");
    }
}
