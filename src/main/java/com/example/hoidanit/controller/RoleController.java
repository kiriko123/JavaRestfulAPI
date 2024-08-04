package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.role.RoleCreateRequestDTO;
import com.example.hoidanit.dto.request.role.RoleUpdateRequestDTO;
import com.example.hoidanit.model.Role;
import com.example.hoidanit.service.RoleService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@Slf4j
@Validated
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<?> addRole(@Valid @RequestBody RoleCreateRequestDTO roleCreateRequestDTO) {
        log.info("Adding role: {}", roleCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleCreateRequestDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles(@Filter Specification<Role> specification, Pageable pageable) {
        log.info("Getting all roles");
        return ResponseEntity.ok().body(roleService.getRoles(specification, pageable));
    }

    @PutMapping
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleUpdateRequestDTO roleUpdateRequestDTO) {
        log.info("Updating role: {}", roleUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleService.updateRole(roleUpdateRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@Min(1) @PathVariable Long id) {
        log.info("Deleting role: {}", id);
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@Min(1) @PathVariable Long id) {
        log.info("Getting role by id: {}", id);
        return ResponseEntity.ok().body(roleService.getRole(id));
    }
}
