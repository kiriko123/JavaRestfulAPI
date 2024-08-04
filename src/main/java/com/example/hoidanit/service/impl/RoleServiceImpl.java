package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.role.RoleCreateRequestDTO;
import com.example.hoidanit.dto.request.role.RoleUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.model.Permission;
import com.example.hoidanit.model.Role;
import com.example.hoidanit.repository.PermissionRepository;
import com.example.hoidanit.repository.RoleRepository;
import com.example.hoidanit.service.RoleService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Role getRole(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Override
    public Role createRole(RoleCreateRequestDTO roleCreateRequestDTO) {

        if (roleRepository.existsByName(roleCreateRequestDTO.getName())) {
            throw new EntityExistsException("Role name already exists");
        }

        List<Permission> permissions = new ArrayList<>();

        for (RoleCreateRequestDTO.Permission permission : roleCreateRequestDTO.getPermissions()) {
            if (permissionRepository.existsById(permission.getId())) {
                permissions.add(permissionRepository.findById(permission.getId()).orElseThrow(()
                        -> new EntityExistsException("Permission not found")));
            }
        }

        return roleRepository.save(
                Role.builder()
                        .name(roleCreateRequestDTO.getName())
                        .description(roleCreateRequestDTO.getDescription())
                        .permissions(permissions)
                        .active(roleCreateRequestDTO.isActive())
                        .build()
        );
    }

    @Override
    public Role updateRole(RoleUpdateRequestDTO roleUpdateRequestDTO) {

        Role role = getRole(roleUpdateRequestDTO.getId());

        if (!roleRepository.existsById(roleUpdateRequestDTO.getId())) {
            throw new EntityNotFoundException("Role not found");
        }
        if (!roleUpdateRequestDTO.getName().equals(role.getName())) {

            if (roleRepository.existsByName(roleUpdateRequestDTO.getName())) {
                throw new EntityExistsException("Role name already exists");
            }

            role.setName(roleUpdateRequestDTO.getName());
        }

        List<Permission> permissions = new ArrayList<>();

        for (RoleCreateRequestDTO.Permission permission : roleUpdateRequestDTO.getPermissions()) {
            if (permissionRepository.existsById(permission.getId())) {
                permissions.add(permissionRepository.findById(permission.getId()).orElseThrow(()
                        -> new EntityExistsException("Permission not found")));
            }
        }
        role.setPermissions(permissions);
        role.setDescription(roleUpdateRequestDTO.getDescription());
        role.setActive(roleUpdateRequestDTO.isActive());

        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(long id) {
        Role role = getRole(id);
        role.getPermissions().forEach(p -> p.getRoles().remove(role));
        roleRepository.delete(role);
    }

    @Override
    public ResultPaginationResponse getRoles(Specification<Role> specification, Pageable pageable) {

        Page<Role> roles = roleRepository.findAll(specification, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(roles.getTotalElements())
                .pages(roles.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(roles.getContent())
                .build();
    }
}
