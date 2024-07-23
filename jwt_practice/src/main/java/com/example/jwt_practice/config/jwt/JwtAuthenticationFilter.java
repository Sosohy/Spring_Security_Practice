package com.example.jwt_practice.config.jwt;

import com.auth0.jwt.JWT;
import com.example.jwt_practice.config.auth.PrincipalDetails;
import com.example.jwt_practice.model.User;
import com.example.jwt_practice.model.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 존재
// /login 요청해서 username, password 전송하면(post)
// UsernamePasswordAuthenticationFilter 동작함
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인 시도 중");

        // 1. username, pw 받아서
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 정상인지 로그인 시도. AuthenticationManager로 로그인(인증) 시도
            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후, 정상이면 authentication이 리턴됨
            Authentication authentication = authenticationManager.authenticate(token);

            // 로그인이 정상적으로 되면  PrincipalDetails 객체를 얻음
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨 : " + principalDetails.getUser());

            // authentication 객체가 session영역에 저장을 해야하고 그 방법이 return 해주면 됨
            // 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리 때문에 session 넣어줌
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {

        System.out.println("successfulAuthentication 실행 -> 인증 완료");
        // 인증된 사용자 정보를 PrincipalDetails 객체에서 가져옴
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // JWT 토큰 생성
        String jwtToken = jwtUtil.createToken(principalDetails.getUser().getUsername());

        // 응답 헤더에 생성된 JWT 토큰을 추가
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
