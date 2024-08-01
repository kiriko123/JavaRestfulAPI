package com.example.hoidanit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class CompanyRequestDTO implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String address;
    @NotBlank
    private String logo;
}
