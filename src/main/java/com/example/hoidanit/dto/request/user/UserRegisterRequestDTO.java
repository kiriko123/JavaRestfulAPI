package com.example.hoidanit.dto.request.user;

import com.example.hoidanit.util.constant.GenderEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserRegisterRequestDTO implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotNull
    @Min(18)
    private int age;
    private GenderEnum gender;
    @NotBlank
    public String address;
}
