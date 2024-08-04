package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.LoginRequestDTO;
import com.example.hoidanit.dto.response.LoginResponse;
import com.example.hoidanit.model.User;
import com.example.hoidanit.service.AuthService;
import com.example.hoidanit.service.UserService;
import com.example.hoidanit.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityUtil securityUtil;

    private final UserService userService;

    @Value("${khang.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @Override
    public LoginResponse login(LoginRequestDTO loginRequestDTO) {

        //nap input us pw vao
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        //xac thuc nguoi dung, can viet ham loadbyusername trong UserDetailCustom
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //set user's data
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse loginResponse = new LoginResponse();
        User currentUser = userService.findByEmail(loginRequestDTO.getUsername());
        if (currentUser != null) {
            LoginResponse.UserLogin userLogin = new
                    LoginResponse.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getUsername(), currentUser.getRole());
            loginResponse.setUser(userLogin);
        }

        //create access token
        String accessToken =  securityUtil.createAccessToken(authentication.getName(), loginResponse);

        loginResponse.setAccessToken(accessToken);

        return loginResponse;
    }

    @Override
    public LoginResponse.UserGetAccount getUserLogin(String email) {
        User currentUser = userService.findByEmail(email);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();

        LoginResponse.UserGetAccount userGetAccount = new LoginResponse.UserGetAccount();

        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setName(currentUser.getUsername());
            userLogin.setRole(currentUser.getRole());
            userGetAccount.setUser(userLogin);
        }
        return userGetAccount;
    }

    @Override
    public LoginResponse getRefreshToken(String refreshToken) {
        return null;
    }
}
