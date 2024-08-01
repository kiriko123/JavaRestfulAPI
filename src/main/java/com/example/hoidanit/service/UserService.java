package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.UserCreateRequestDTO;
import com.example.hoidanit.dto.request.UserRequestDTO;
import com.example.hoidanit.dto.request.UserUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.dto.response.UserResponse;
import com.example.hoidanit.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    UserResponse save(UserCreateRequestDTO userCreateRequestDTO);
    User findByUserName(String username);
    UserResponse update(long id, UserUpdateRequestDTO userUpdateRequestDTO);
    void delete(Long id);
    ResultPaginationResponse findAll(Specification<User> spec, Pageable pageable);
    User findById(Long id);
    User findByEmail(String email);
    void updateUserToken(String token, String email);
    User getUserByEmailAndRefreshToken(String email, String refreshToken);
}
