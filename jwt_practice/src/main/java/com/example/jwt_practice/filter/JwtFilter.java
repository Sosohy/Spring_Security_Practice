package com.example.jwt_practice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("필터1 - jwt 인증");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // id, pw 정상적으로 들어와서 로그인이 완료되면 토큰 생성 및 응답
        // 요청할 때마다 header에 Authorizaion value 값으로 가지고 옴
        // 넘어온 토큰이 내가 만든 토큰이 맞는지 검증(RSA, HS256)
        if(req.getMethod().equals("POST")){
            System.out.println("post요청");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);

            if(headerAuth.equals("cos")){
                filterChain.doFilter(req, res);
            }else{
                System.out.println("인증 안됨");
            }
        }

    }
}
