package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.UserCreateRequestDTO;
import com.example.hoidanit.dto.request.UserRequestDTO;
import com.example.hoidanit.dto.request.UserUpdateRequestDTO;
import com.example.hoidanit.dto.response.UserResponse;
import com.example.hoidanit.model.User;
import com.example.hoidanit.service.UserService;
import com.example.hoidanit.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
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
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> createUser( @Validated @RequestBody UserCreateRequestDTO userRequestDTO) {
        log.info("Create user : {}", userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Min(1) @PathVariable Long id, @Validated @RequestBody UserUpdateRequestDTO userRequestDTO) {
        log.info("Update user : {}", userRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.update(id, userRequestDTO));
    }
    @GetMapping("")
    @ApiMessage("Get all users")
    public ResponseEntity<?> getAllUsers(@Filter Specification<User> specification,
                                         Pageable pageable) {
        log.info("getAllUsers called");
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(specification, pageable));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Min(1) @PathVariable Long id) {
        log.info("Delete user : {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Min(1) @PathVariable Long id) {
        log.info("getUserById called");
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.fromUserToUserResponse(user));
    }
}
