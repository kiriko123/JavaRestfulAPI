package com.example.hoidanit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SkillRequestDTO {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
}
