package com.example.jwt_practice.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt_practice.model.UserRole;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final String secretKey;
    private final Long expiredMs;

    public JwtUtil(Environment environment) {
        this.secretKey = environment.getProperty("token.secret");
        this.expiredMs = Long.valueOf(environment.getProperty("token.expiration_time"));
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredMs))
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(secretKey));
    }

}
