package com.example.hoidanit.dto.response;

import com.example.hoidanit.model.User;
import com.example.hoidanit.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder
@Setter
public class UserResponse implements Serializable {

    private long id;

    private String username;

    private String email;

    private GenderEnum gender;

    private String address;

    private int age;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;
    private Company company;

    public static UserResponse fromUserToUserResponse(User user) {

        UserResponse userResponse =  UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .gender(user.getGender())
                .address(user.getAddress())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();

        if (user.getCompany() != null) {
            userResponse.setCompany(Company.builder()
                    .id(user.getCompany().getId())
                    .name(user.getCompany().getName())
                    .build());
        }else{
            userResponse.setCompany(Company.builder()
                    .id(0)
                    .name(null)
                    .build());
        }

        return userResponse;

    }
    @Getter
    @Builder
    public static class Company{
        private long id;
        private String name;
    }
}
