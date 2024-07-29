package com.example.jwt_practice.config;

import com.example.jwt_practice.config.jwt.JwtAuthenticationFilter;
import com.example.jwt_practice.config.jwt.JwtAuthorizationFilter;
import com.example.jwt_practice.config.jwt.JwtUtil;
import com.example.jwt_practice.config.oauth.PrincipalOauth2UserService;
import com.example.jwt_practice.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    // CORS(Cross-Origin Resource Sharing)를 처리하기 위한 필터
    private final CorsFilter corsFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    public SecurityConfig(CorsFilter corsFilter, AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
        this.corsFilter = corsFilter;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

//        http.addFilterBefore(new JwtFilter(), JwtAuthenticationFilter.class);

        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtUtil));

        http.csrf(AbstractHttpConfigurer::disable)
                // 세션을 사용하지 않고, JWT를 사용하여 무상태(stateless) 서버로 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CORS 설정을 통해 다른 도메인에서의 요청을 허용
                .addFilter(corsFilter)  // @CrossOrigin(인증X), 시큐리티 필터에 등록(인증O)
                .formLogin(formLogin  ->                        // 로그인 설정
                        formLogin.loginPage("/loginForm")        // 권한 없을 경우, 로그인 페이지로
                                .loginProcessingUrl("/login")   // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                                .defaultSuccessUrl("/")          // 로그인 페이지로 접속했을 경우, default 페이지로 -> 아닌 경우 요청했던 페이지로 감
                                //.formLogin(formLogin -> formLogin.disable())  // 기본적인 폼 로그인을 비활성화
                ).httpBasic(httpBasic -> httpBasic.disable())  // HTTP 기본 인증을 비활성화
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                                .requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                                .anyRequest().permitAll())
                .oauth2Login(oauthLogin ->
                        oauthLogin.loginPage("/loginForm")
                                .userInfoEndpoint(userInfo ->  // 구글 로그인 완료 후의 후처리가 필요 Tip. 코드X, (액세스 토큰 + 사용자 프로필 정보O)
                                        userInfo.userService(principalOauth2UserService)
                                ));

        return http.build();
    }

}
