package com.example.jwt_practice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String roles;
    @CreationTimestamp
    private Timestamp createDate;

    private String provider;
    private String providerId;

    public User() {
    }

    public User(int id, String username, String password, String email, String role, Timestamp createDate,
                String provider, String providerId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = role;
        this.createDate = createDate;
        this.provider = provider;
        this.providerId = providerId;
    }

    @Builder
    public User(String username, String password, String email, String role, Timestamp createDate,
                String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = role;
        this.createDate = createDate;
        this.provider = provider;
        this.providerId = providerId;
    }

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
