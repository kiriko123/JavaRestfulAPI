package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.CompanyRequestDTO;
import com.example.hoidanit.model.Company;
import com.example.hoidanit.service.CompanyService;
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
@Validated
@RequestMapping("/api/v1/companies")
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/")
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        log.info("Create company request: {}", companyRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(companyRequestDTO));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCompanies(
            @Filter Specification<Company> specification,
            Pageable pageable) {
        log.info("Get all companies");
        return ResponseEntity.ok().body(companyService.getAllCompanies(specification, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@Valid @RequestBody CompanyRequestDTO companyRequestDTO, @Min(1) @PathVariable long id) {
        log.info("Update company request: {}", companyRequestDTO);
        return ResponseEntity.accepted().body(companyService.updateCompany(id, companyRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@Min(1) @PathVariable long id) {
        log.info("Delete company request: {}", id);
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@Min(1) @PathVariable long id) {
        log.info("Get company request: {}", id);
        return ResponseEntity.ok().body(companyService.getCompany(id));
    }
}
