package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.permission.PermissionCreateRequestDTO;
import com.example.hoidanit.dto.request.permission.PermissionUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.model.Permission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionService {
    Permission getPermission(long id);
    Permission savePermission(PermissionCreateRequestDTO permissionCreateRequestDTO);
    Permission updatePermission(PermissionUpdateRequestDTO permissionUpdateRequestDTO);
    ResultPaginationResponse getPermissions(Specification<Permission> specification, Pageable pageable);
    void deletePermission(long id);
}
