package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.LoginRequestDTO;
import com.example.hoidanit.dto.request.UserCreateRequestDTO;
import com.example.hoidanit.dto.request.user.UserRegisterRequestDTO;
import com.example.hoidanit.dto.response.LoginResponse;
import com.example.hoidanit.model.User;
import com.example.hoidanit.service.AuthService;
import com.example.hoidanit.service.UserService;
import com.example.hoidanit.util.SecurityUtil;
import com.example.hoidanit.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityUtil securityUtil;

    private final UserService userService;

    private final AuthService authService;

    @Value("${khang.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<?> auth(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Login request: {}", loginRequestDTO);
        /*
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
                    LoginResponse.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getUsername());
            loginResponse.setUser(userLogin);
        }

        //create access token
        String accessToken =  securityUtil.createAccessToken(authentication.getName(), loginResponse);

        loginResponse.setAccessToken(accessToken);

        */

        LoginResponse loginResponse = authService.login(loginRequestDTO);

        //create refresh token
        String refreshToken = securityUtil.createRefreshToken(loginRequestDTO.getUsername(), loginResponse);
        //update user
        userService.updateUserToken(refreshToken, loginRequestDTO.getUsername());

        //set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @GetMapping("/account")
    @ApiMessage("Get account message")
    public ResponseEntity<?> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        /*User currentUser = userService.findByEmail(email);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();

        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setUsername(currentUser.getUsername());
        }*/
        LoginResponse.UserGetAccount userLogin = authService.getUserLogin(email);
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<?> getRefreshToken(@CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token){
        log.info("Call refresh_token");
        if (refresh_token.equals("abc")){
            throw new AccessDeniedException("Ko co refresh token trong cookies");
        }
        //check valid
        Jwt decodedToken = securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

        //check user by token and email

        User currentUser = userService.getUserByEmailAndRefreshToken(email, refresh_token);
        if (currentUser == null){
            throw new AccessDeniedException("Truy cap khong hop le");
        }

        //tao token moi va set vo database a

        LoginResponse loginResponse = new LoginResponse();
        User currentUserDB = userService.findByEmail(email);
        if (currentUserDB != null) {
            LoginResponse.UserLogin userLogin = new
                    LoginResponse.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(), currentUserDB.getName(), currentUserDB.getRole());
            loginResponse.setUser(userLogin);
        }

        //create access token
        String accessToken =  securityUtil.createAccessToken(email, loginResponse);

        loginResponse.setAccessToken(accessToken);

        //create refresh token
        String newRefreshToken = securityUtil.createRefreshToken(email, loginResponse);
        //update user
        userService.updateUserToken(newRefreshToken, email);

        //set cookies
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout user")
    public ResponseEntity<?> logout() {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.isEmpty()){
            throw new AccessDeniedException("Access token khong hop le");
        }
        //update refresh token
        userService.updateUserToken(null, email);

        // remove refresh token cookies
        ResponseCookie deleteSpringCookies = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookies.toString())
                .body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO){
        log.info("Create user : {}", userRegisterRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userRegisterRequestDTO));
    }

}
