package com.example.hoidanit.repository;

import com.example.hoidanit.model.Company;
import com.example.hoidanit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByEmailAndRefreshToken(String email, String refreshToken);
    List<User> findByCompany(Company company);
}
