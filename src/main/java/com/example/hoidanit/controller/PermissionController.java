package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.permission.PermissionCreateRequestDTO;
import com.example.hoidanit.dto.request.permission.PermissionUpdateRequestDTO;
import com.example.hoidanit.model.Permission;
import com.example.hoidanit.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;
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
@RequestMapping("/api/v1/permissions")
@Slf4j
@Validated
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<?> addPermission(@Validated @RequestBody PermissionCreateRequestDTO permissionCreateRequestDTO) {
        log.info("Add permission: {}", permissionCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.savePermission(permissionCreateRequestDTO));
    }

    @PutMapping
    public ResponseEntity<?> updatePermission(@Validated @RequestBody PermissionUpdateRequestDTO permissionUpdateRequestDTO) {
        log.info("Update permission: {}", permissionUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(permissionService.updatePermission(permissionUpdateRequestDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllPermissions(@Filter Specification<Permission> specification, Pageable pageable) {
        log.info("Get all permissions");
        return ResponseEntity.ok().body(permissionService.getPermissions(specification, pageable));
    }

}
