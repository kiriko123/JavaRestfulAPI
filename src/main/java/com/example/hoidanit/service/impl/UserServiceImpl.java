package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.UserCreateRequestDTO;
import com.example.hoidanit.dto.request.UserUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.dto.response.UserResponse;
import com.example.hoidanit.exception.InvalidDataException;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Company;
import com.example.hoidanit.model.Role;
import com.example.hoidanit.model.User;
import com.example.hoidanit.repository.CompanyRepository;
import com.example.hoidanit.repository.RoleRepository;
import com.example.hoidanit.repository.UserRepository;
import com.example.hoidanit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse save(UserCreateRequestDTO userRequestDTO) {

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new InvalidDataException("Email already exists");
        }
        Company company = new Company();
        if (userRequestDTO.getCompany() != null) {
            company = companyRepository.findById(userRequestDTO.getCompany().getId()).orElse(null);
        }

        Role role = new Role();
        if (userRequestDTO.getRole() != null) {
            role = roleRepository.findById(userRequestDTO.getRole().getId()).orElse(null);
        }

        User user = userRepository.save(User.builder()
                .name(userRequestDTO.getName())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .age(userRequestDTO.getAge())
                .address(userRequestDTO.getAddress())
                .gender(userRequestDTO.getGender())
                .email(userRequestDTO.getEmail())
                .company(company)
                .role(role)
                .build());

        return UserResponse.fromUserToUserResponse(user);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public UserResponse update(long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = findById(id);
        if (userUpdateRequestDTO.getCompany() != null) {
            Company company = companyRepository.findById(userUpdateRequestDTO.getCompany().getId()).orElse(null);
            if (company != null) {
                user.setCompany(company);
            }
        }
        if (userUpdateRequestDTO.getRole() != null) {
            Role role = roleRepository.findById(userUpdateRequestDTO.getRole().getId()).orElse(null);
            if (role != null) {
                user.setRole(role);
            }
        }
        user.setName(userUpdateRequestDTO.getName());
        user.setAddress(userUpdateRequestDTO.getAddress());
        user.setGender(userUpdateRequestDTO.getGender());
        user.setAge(userUpdateRequestDTO.getAge());

        return UserResponse.fromUserToUserResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        findById(id);
        userRepository.deleteById(id);
    }

    @Override
    public ResultPaginationResponse findAll(Specification<User> spec, Pageable pageable) {

        Page<User> users = userRepository.findAll(spec, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(users.getTotalElements())
                .pages(users.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        List<UserResponse> userResponses = users.getContent().stream().map(UserResponse::fromUserToUserResponse).toList();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(userResponses)
                .build();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUserToken(String token, String email) {
        User user = findByEmail(email);
        user.setRefreshToken(token);
        userRepository.save(user);
    }

    @Override
    public User getUserByEmailAndRefreshToken(String email, String refreshToken) {
        return userRepository.findByEmailAndRefreshToken(email, refreshToken);
    }

    @Override
    public long countAllUser() {
        return userRepository.count();
    }
}
