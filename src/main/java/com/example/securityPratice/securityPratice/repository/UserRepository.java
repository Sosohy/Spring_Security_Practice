package com.example.securityPratice.securityPratice.repository;

import com.example.securityPratice.securityPratice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
