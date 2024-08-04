package com.example.hoidanit.dto.request.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class PermissionCreateRequestDTO implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private String apiPath;
    @NotBlank
    private String method;
    @NotBlank
    private String module;
}
