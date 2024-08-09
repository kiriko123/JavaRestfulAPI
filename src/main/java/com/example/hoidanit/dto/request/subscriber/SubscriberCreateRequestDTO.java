package com.example.hoidanit.dto.request.subscriber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class SubscriberCreateRequestDTO implements Serializable {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;

    private List<Skill> skills;

    @Getter
    public static class Skill{
        @NotNull
        @Min(1)
        private long id;
    }
}
