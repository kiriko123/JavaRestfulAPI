package com.example.hoidanit.dto.request;

import com.example.hoidanit.util.constant.GenderEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
public class UserCreateRequestDTO implements Serializable {
    @NotBlank
    @Size(min = 6, max = 20)
    private String username;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private GenderEnum gender;

    private String address;
    @NotNull
    @Min(1)
    private int age;

    private Company company;

    private Role role;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Company{
        private long id;
        private String name;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Role{
        private long id;
    }
}
