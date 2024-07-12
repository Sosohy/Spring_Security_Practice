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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록
public class SecurityConfig{

    @Bean  // 리턴되는 오브젝트를 IoC로 등록
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

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
                                .anyRequest().permitAll()        // 누구나 접근 가능
                ).formLogin(formLogin  ->                        // 로그인 설정
                        formLogin.loginPage("/loginForm")        // 권한 없을 경우, 로그인 페이지로
                                 .loginProcessingUrl("/login")   // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                                .defaultSuccessUrl("/")          // 로그인 페이지로 접속했을 경우, default 페이지로 -> 아닌 경우 요청했던 페이지로 감
                );

        return http.build();
    }


}
