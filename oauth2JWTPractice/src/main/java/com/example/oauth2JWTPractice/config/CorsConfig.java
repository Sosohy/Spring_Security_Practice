package com.example.oauth2JWTPractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {  // 로그인의 경우 시큐리티 필터만 통과 후 응답이 되기 때문에 SecurityConfig에 설정한 CORS 값으로 진행

    @Bean
    public CorsFilter corsFilter() {
        // CORS 설정을 적용할 URL 패턴을 관리하는 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 허용할 오리진(출처) 패턴 설정
        List<String> allowStringList = Collections.singletonList("*");

        // 노출할 헤더 설정 (클라이언트에게 노출할 추가적인 응답 헤더)
        List<String> exposedHeaders = List.of("Authorization", "UserRole", "Set-Cookie");

        // CORS 구성 객체 생성 및 설정
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(allowStringList); // 모든 오리진 허용
        configuration.setAllowedMethods(allowStringList); // 모든 HTTP 메소드(put, patch, get, post) 허용
        configuration.setAllowedHeaders(allowStringList); // 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용 (쿠키 및 HTTP 인증 정보)
        configuration.setMaxAge(3600L); // preflight 요청 결과를 캐시할 시간 (1시간)
        configuration.setExposedHeaders(exposedHeaders); // 클라이언트에게 노출할 추가적인 응답 헤더 설정

        // 위에서 생성한 설정을 URL 패턴에 적용
        source.registerCorsConfiguration("/**", configuration);

        // CorsFilter 객체 생성하여 반환
        return new CorsFilter(source);
    }

}
