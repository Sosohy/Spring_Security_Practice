package com.example.oauth2JWTPractice.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String role;
    private String name;
    private String email;
    private String username;
}
