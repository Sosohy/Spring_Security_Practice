package com.example.jwt_practice.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 존재
// /login 요청해서 username, password 전송하면(post)
// UsernamePasswordAuthenticationFilter 동작함
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인 시도 중");

        // 1. username, pw 받아서

        // 2. 정상인지 로그인 시도. AuthenticationManager로 로그인 시도 하면
        // PrincipalDetailsService가 호출 loadUserByUsername() 실행

        // 3. PrincipalDetails를 세션에 담고 (-> 권한 관리를 위해)

        // 4. JWT 토큰을 만들어서 응답

        return super.attemptAuthentication(request, response);
    }
}
