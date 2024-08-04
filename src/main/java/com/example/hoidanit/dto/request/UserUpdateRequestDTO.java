package com.example.hoidanit.dto.request;

import com.example.hoidanit.util.constant.GenderEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class UserUpdateRequestDTO implements Serializable {
    @NotBlank
    @Size(min = 6, max = 20)
    private String username;
    @NotNull
    private GenderEnum gender;
    @NotBlank
    private String address;
    @NotNull
    @Min(1)
    private int age;

    private Company company;

    public Role role;

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
