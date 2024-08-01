package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.CompanyRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.model.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CompanyService {
    Company getCompany(long id);
    Company createCompany(CompanyRequestDTO companyRequestDTO);
    Company updateCompany(long id, CompanyRequestDTO companyRequestDTO);
    void deleteCompany(long id);
    ResultPaginationResponse getAllCompanies(Specification<Company> spec, Pageable pageable);
}
