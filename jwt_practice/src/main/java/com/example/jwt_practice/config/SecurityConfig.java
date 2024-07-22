package com.example.jwt_practice.config;

import com.example.jwt_practice.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    // CORS(Cross-Origin Resource Sharing)를 처리하기 위한 필터
    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        // JWT 필터
        http.addFilterBefore(new JwtFilter(), BasicAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable)
                // 세션을 사용하지 않고, JWT를 사용하여 무상태(stateless) 서버로 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CORS 설정을 통해 다른 도메인에서의 요청을 허용
                .addFilter(corsFilter)  // @CrossOrigin(인증X), 시큐리티 필터에 등록(인증O)
                .formLogin(formLogin -> formLogin.disable())  // 기본적인 폼 로그인을 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())  // HTTP 기본 인증을 비활성화
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                                .requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                                .anyRequest().permitAll());

        return http.build();
    }

}
