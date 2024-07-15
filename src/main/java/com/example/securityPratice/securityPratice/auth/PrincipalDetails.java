package com.example.securityPratice.securityPratice.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행
// 로그인을 진행이 완료되면 시큐리티 session을 만들어줌(Security ContextHolder)
// 오브젝트 타입 -> Authentication 타입 객체
// Authentication 안에 user정보가 있어야 함
// User 오브젝트 타입 -> UserDetails 타입 객체

// Security Session -> Authentication -> UserDetails(PrincipalDetails)

import com.example.securityPratice.securityPratice.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 컴포지션 -> 클래스의 구성요소로 쓰임
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(User user){
        this.user = user;
    }

    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
