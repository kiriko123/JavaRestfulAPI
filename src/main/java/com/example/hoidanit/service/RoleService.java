package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.role.RoleCreateRequestDTO;
import com.example.hoidanit.dto.request.role.RoleUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.model.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    Role getRole(long id);
    Role createRole(RoleCreateRequestDTO roleCreateRequestDTO);
    Role updateRole(RoleUpdateRequestDTO roleUpdateRequestDTO);
    void deleteRole(long id);
    ResultPaginationResponse getRoles(Specification<Role> specification, Pageable pageable);
}
