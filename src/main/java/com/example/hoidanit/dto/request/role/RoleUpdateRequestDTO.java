package com.example.hoidanit.dto.request.role;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
@Getter
public class RoleUpdateRequestDTO {

    @NotNull
    @Min(1)
    private long id;

    @NotBlank
    private String name;
    private String description;
    private boolean active;

    private List<RoleCreateRequestDTO.Permission> permissions;

    @Getter
    public static class Permission {
        private long id;
    }

}
