package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.permission.PermissionCreateRequestDTO;
import com.example.hoidanit.dto.request.permission.PermissionUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Permission;
import com.example.hoidanit.repository.PermissionRepository;
import com.example.hoidanit.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public Permission getPermission(long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }

    @Override
    public Permission savePermission(PermissionCreateRequestDTO permissionCreateRequestDTO) {

        if (permissionRepository.existsByName(permissionCreateRequestDTO.getName())) {
            throw new IllegalArgumentException("Permission name already exists");
        }
        if (permissionRepository.existsByApiPathAndMethodAndModule(permissionCreateRequestDTO.getApiPath(),
                permissionCreateRequestDTO.getMethod(),
                permissionCreateRequestDTO.getModule())) {
            throw new IllegalArgumentException("Something already exists");
        }

        return permissionRepository.save(
                Permission.builder()
                        .name(permissionCreateRequestDTO.getName())
                        .apiPath(permissionCreateRequestDTO.getApiPath())
                        .method(permissionCreateRequestDTO.getMethod())
                        .module(permissionCreateRequestDTO.getModule())
                        .build()
        );
    }

    @Override
    public Permission updatePermission(PermissionUpdateRequestDTO permissionUpdateRequestDTO) {

        Permission permission = getPermission(permissionUpdateRequestDTO.getId());

        if (!permission.getName().equals(permissionUpdateRequestDTO.getName())) {
            if (permissionRepository.existsByName(permissionUpdateRequestDTO.getName())) {
                throw new IllegalArgumentException("Permission name already exists");
            }
            permission.setName(permissionUpdateRequestDTO.getName());
        }

        if (!permission.getApiPath().equals(permissionUpdateRequestDTO.getApiPath()) ||
                !permission.getMethod().equals(permissionUpdateRequestDTO.getMethod()) ||
                !permission.getModule().equals(permissionUpdateRequestDTO.getModule())
        ) {
            if (permissionRepository.existsByApiPathAndMethodAndModule(permissionUpdateRequestDTO.getApiPath(),
                    permissionUpdateRequestDTO.getMethod(),
                    permissionUpdateRequestDTO.getModule())) {
                throw new IllegalArgumentException("Something already exists");
            }
            permission.setApiPath(permissionUpdateRequestDTO.getApiPath());
            permission.setMethod(permissionUpdateRequestDTO.getMethod());
            permission.setModule(permissionUpdateRequestDTO.getModule());
        }

        return permissionRepository.save(permission);
    }

    @Override
    public ResultPaginationResponse getPermissions(Specification<Permission> specification, Pageable pageable) {

        Page<Permission> permissions = permissionRepository.findAll(specification, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(permissions.getTotalElements())
                .pages(permissions.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(permissions.getContent())
                .build();
    }

    @Override
    public void deletePermission(long id) {
        Permission permission = getPermission(id);
        permission.getRoles().forEach(r -> r.getPermissions().remove(permission));
        permissionRepository.delete(permission);
    }
}
