package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.LoginRequestDTO;
import com.example.hoidanit.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequestDTO loginRequestDTO);
    LoginResponse.UserGetAccount getUserLogin(String email);
    LoginResponse getRefreshToken(String refreshToken);
}
