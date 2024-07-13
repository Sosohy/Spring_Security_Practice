package com.example.securityPratice.securityPratice.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest = " + userRequest.getAccessToken());
        System.out.println("userRequest = " + userRequest.getClientRegistration());
        System.out.println("userRequest = " + super.loadUser(userRequest).getAttributes());

        // 회원가입 강제 진행 예정
        return super.loadUser(userRequest);
    }
}
