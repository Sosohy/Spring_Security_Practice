package com.example.securityPratice.securityPratice.oauth;

import com.example.securityPratice.securityPratice.auth.PrincipalDetails;
import com.example.securityPratice.securityPratice.model.User;
import com.example.securityPratice.securityPratice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    // 함수 종료시 AuthenticationPrincipal 어노테이션이 만들어짐
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("getAccessToken = " + userRequest.getAccessToken().getTokenValue());
        System.out.println("getClientRegistration = " + userRequest.getClientRegistration());  // registrationId로 어떤 OAuth로 로그인

        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> 코드를 리턴(OAuth-Client 라이브러리) -> Accesstoken 요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필 받아줌
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("userRequest = " + oauth2User.getAttributes());

        // 회원가입 강제 진행 예정
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oauth2User.getAttribute("sub");
        String username = provider + "_" + providerId;  // google_1097400000000

        String password = bCryptPasswordEncoder.encode("defaultPW");
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){  // 회원가입
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
