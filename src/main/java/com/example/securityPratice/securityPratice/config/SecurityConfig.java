package com.example.securityPratice.securityPratice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 다양한 보안 설정 구성
        http
                .csrf(AbstractHttpConfigurer::disable)      // CSRF 보호 비활성화
                .authorizeHttpRequests(authorizeRequest ->  // 요청에 대한 권한 설정
                        authorizeRequest
                                .requestMatchers("/user/**").authenticated()   // 인증된 사용자만 접근 가능
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")  // ADMIN MANAGER만 접근 가능
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")  // ADMIN만 접근 가능
                                .anyRequest().permitAll()  // 누구나 접근 가능
                ).formLogin(formLogin  ->   // 로그인 설정
                        formLogin.loginPage("/login"));  // 권한 없을 경우, 로그인 페이지로

        return http.build();
    }


}
