package com.example.oauth2JWTPractice.repository;

import com.example.oauth2JWTPractice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
