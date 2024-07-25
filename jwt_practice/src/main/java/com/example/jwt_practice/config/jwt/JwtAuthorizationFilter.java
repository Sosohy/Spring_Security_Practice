package com.example.jwt_practice.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt_practice.config.auth.PrincipalDetails;
import com.example.jwt_practice.model.User;
import com.example.jwt_practice.model.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// 시큐리티가 filter를 가지고 있는데 그 필터 중에서 BasicAuthenticationfilter라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
// 만약 권한 인증이 필요한 주소가 아니라면 해당 필터를 타지 않음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtil jwtUtil;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");
        
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader = " + jwtHeader);

        // header가 있는지 확인
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){ // 헤더가 없거나 Bearer가 아닌 경우
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = jwtHeader.replace("Bearer ", "");
        System.out.println("jwtToken = " + jwtToken);

        String username = jwtUtil.getUsername(jwtToken);
        System.out.println("username = " + username);

        // 서명이 정상적으로 됨
        if(username != null){
            User userEntity = new User();
            userEntity.setUsername(username);
            userEntity.setRoles(UserRole.ROLE_ADMIN.toString());

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            // jwt 토큰 서명을 통해 서명이 정상이면 Authentication 객체를 만들어준다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}
