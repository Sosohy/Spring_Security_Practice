package com.example.jwt_practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){

        // 특정 URL 패턴에 대한 CORS 설정을 등록하기 위한 소스
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();  // CORS 정책을 정의하는 객체
        config.setAllowCredentials(true);  // 클라이언트 측에서 자바스크립트를 통해 응답을 처리할 수 있도록 설정
        config.addAllowedOrigin("*");      // 모든 ip에 응답 허용
        config.addAllowedHeader("*");      // 모든 header에 응답 허용
        config.addAllowedMethod("*");      // 모든 post, get, put, delete, patch 요청 허용

        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
