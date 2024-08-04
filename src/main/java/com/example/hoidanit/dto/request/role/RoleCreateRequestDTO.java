package com.example.hoidanit.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class RoleCreateRequestDTO implements Serializable {
    @NotBlank
    private String name;
    private String description;
    private boolean active;

    private List<Permission> permissions;

    @Getter
    public static class Permission {
        private long id;
    }
}
