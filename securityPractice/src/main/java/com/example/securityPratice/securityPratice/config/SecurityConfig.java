package com.example.securityPratice.securityPratice.config;

import com.example.securityPratice.securityPratice.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//1. 코드받기(인증) 2. 액세스토큰(권한) 3. 사용자프로필 정보를 가져오고
// 4-1. 그 정보를 토대로 회원가입 자동 진행하기도 함
// 4-2. 이메일 전화번호 이름 아이디 -> 집 주소, 등급 등 필요하기도

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록
// secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

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
                ).oauth2Login(oauthLogin ->
                        oauthLogin.loginPage("/loginForm")
                                  .userInfoEndpoint(userInfo ->  // 구글 로그인 완료 후의 후처리가 필요 Tip. 코드X, (액세스 토큰 + 사용자 프로필 정보O)
                                          userInfo.userService(principalOauth2UserService)
                                ));

        return http.build();
    }


}
