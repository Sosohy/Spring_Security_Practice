package com.example.jwt_practice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    // 여기까지 세팅 완료
    @GetMapping("/home")
    public String home(){
        return "<h1>HOME</h1>";
    }
}
