package com.example.securityPratice.securityPratice.controller;

import com.example.securityPratice.securityPratice.auth.PrincipalDetails;
import com.example.securityPratice.securityPratice.model.User;
import com.example.securityPratice.securityPratice.repository.UserRepository;
import org.apache.catalina.authenticator.SpnegoAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails){ // DI(의존성 주입)
        System.out.println("/test/login --------------------------");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails.getUser());

        System.out.println("userDetails = " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth){ // DI(의존성 주입)
        System.out.println("/test/oauth/login --------------------------");

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication = " + oauth2User.getAttributes());

        return "oauth 세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index(){
        return "index";
    }

    // OAuth로그인/일반 로그인 모두 PrincipalDetails로 받을 수 있음
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){

        user.setRole("ROLE_USER");

        // 패스워드 암호화해서 유저에 다시 세팅
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        // 유저 저장
        userRepository.save(user);

        return "redirect:/loginForm";
    }

//    @Secured("ROLE_ADMIN")
//    @PreAuthorize("hasAnyRole(ROlE_ADMIN, ROLE_MANAGER)")  // 메소드 실행 전 인증(<-> post : 메서드가 실행되고 나서 응답을 보내기 전에 인증)
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

}
