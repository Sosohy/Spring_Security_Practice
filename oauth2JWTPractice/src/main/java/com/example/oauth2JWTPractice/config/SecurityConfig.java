package com.example.oauth2JWTPractice.config;

import com.example.oauth2JWTPractice.jwt.JWTUtil;
import com.example.oauth2JWTPractice.oauth2.CustomSuccessHandler;
import com.example.oauth2JWTPractice.service.CustomOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOauth2UserService oauth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOauth2UserService oauth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {
        this.oauth2UserService = oauth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable)               // csrf disable
                .formLogin((formLogin) -> formLogin.disable())   // formLogin 방식 disable
                .httpBasic((httpBasic) -> httpBasic.disable()); // HTTP Basic 인증 방식 disable

        http.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(oauth2UserService)))
                .successHandler(customSuccessHandler));

        //경로별 인가 작업
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
