package com.example.hoidanit.dto.request.permission;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class PermissionUpdateRequestDTO implements Serializable {
    @NotNull
    @Min(1)
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String apiPath;
    @NotBlank
    private String method;
    @NotBlank
    private String module;
}
