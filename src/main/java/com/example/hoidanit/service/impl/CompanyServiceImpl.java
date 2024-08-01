package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.CompanyRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Company;
import com.example.hoidanit.model.User;
import com.example.hoidanit.repository.CompanyRepository;
import com.example.hoidanit.repository.UserRepository;
import com.example.hoidanit.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    public Company getCompany(long id) {
        return companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    public Company createCompany(CompanyRequestDTO companyRequestDTO) {
        return companyRepository.save(Company.builder()
                .name(companyRequestDTO.getName())
                .address(companyRequestDTO.getAddress())
                .logo(companyRequestDTO.getLogo())
                .description(companyRequestDTO.getDescription())
                .build());
    }

    @Override
    public Company updateCompany(long id, CompanyRequestDTO companyRequestDTO) {
        Company company = getCompany(id);
        company.setName(companyRequestDTO.getName());
        company.setAddress(companyRequestDTO.getAddress());
        company.setLogo(companyRequestDTO.getLogo());
        company.setDescription(companyRequestDTO.getDescription());
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompany(long id) {
        Company company =  getCompany(id);
        List<User> users = userRepository.findByCompany(company);
        if (users != null) {
            userRepository.deleteAll(users);
        }
        companyRepository.deleteById(id);
    }

    @Override
    public ResultPaginationResponse getAllCompanies(Specification<Company> specification, Pageable pageable) {

        Page<Company> companies = companyRepository.findAll(specification, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(companies.getTotalElements())
                .pages(companies.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(companies.getContent())
                .build();
    }
}
