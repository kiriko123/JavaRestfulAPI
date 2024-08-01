package com.example.hoidanit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class UserRequestDTO implements Serializable {
    @NotBlank
    @Size(min = 6, max = 20)
    private String username;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    @NotBlank
    @Email
    private String email;
}
