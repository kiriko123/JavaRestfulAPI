package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.resume.ResumeCreateRequestDTO;
import com.example.hoidanit.dto.request.resume.ResumeUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.dto.response.resume.ResumeCreateResponse;
import com.example.hoidanit.dto.response.resume.ResumeGetResponse;
import com.example.hoidanit.dto.response.resume.ResumeUpdateResponse;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Company;
import com.example.hoidanit.model.Job;
import com.example.hoidanit.model.Resume;
import com.example.hoidanit.model.User;
import com.example.hoidanit.repository.JobRepository;
import com.example.hoidanit.repository.ResumeRepository;
import com.example.hoidanit.repository.UserRepository;
import com.example.hoidanit.service.ResumeService;
import com.example.hoidanit.service.UserService;
import com.example.hoidanit.util.SecurityUtil;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;
    private final UserService userService;

    @Override
    public ResumeCreateResponse createResume(ResumeCreateRequestDTO resumeCreateRequestDTO) {

        //check job
        if (!jobRepository.existsById(resumeCreateRequestDTO.getJob().getId())) {
            throw new ResourceNotFoundException("Job not found with id = " + resumeCreateRequestDTO.getJob().getId());
        }
        //check user
        if (!userRepository.existsById(resumeCreateRequestDTO.getUser().getId())) {
            throw new ResourceNotFoundException("User not found with id = " + resumeCreateRequestDTO.getUser().getId());
        }

        Resume resume = resumeRepository.save(Resume.builder()
                .user(userRepository.findById(resumeCreateRequestDTO.getUser().getId()).orElse(null))
                .job(jobRepository.findById(resumeCreateRequestDTO.getJob().getId()).orElse(null))
                .email(resumeCreateRequestDTO.getEmail())
                .url(resumeCreateRequestDTO.getUrl())
                .status(resumeCreateRequestDTO.getStatus())
                .build());

        return ResumeCreateResponse.builder()
                .id(resume.getId())
                .createdAt(resume.getCreatedAt())
                .createdBy(resume.getCreatedBy())
                .build();
    }

    @Override
    public ResumeUpdateResponse updateResume(ResumeUpdateRequestDTO resumeUpdateRequestDTO) {

        Resume resume = getResume(resumeUpdateRequestDTO.getId());

        resume.setStatus(resumeUpdateRequestDTO.getStatus());

        Resume updatedResume = resumeRepository.save(resume);

        return ResumeUpdateResponse.builder()
                .updatedAt(updatedResume.getUpdatedAt())
                .updatedBy(updatedResume.getUpdatedBy())
                .build();
    }

    @Override
    public void deleteResume(long id) {
        getResume(id);
        resumeRepository.deleteById(id);
    }

    @Override
    public Resume getResume(long id) {
        return resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
    }

    @Override
    public ResumeGetResponse getOne(long id) {
        return ResumeGetResponse.fromResumeToResumeGetResponse(getResume(id));
    }

    @Override
    public ResultPaginationResponse getAll(Specification<Resume> specification, Pageable pageable) {

        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        User currentUser = this.userService.findByEmail(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && !companyJobs.isEmpty()) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(specification);

        Page<Resume> resumes = resumeRepository.findAll(finalSpec, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(resumes.getTotalElements())
                .pages(resumes.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        return ResultPaginationResponse.builder()
                .result(resumes.getContent().stream().map(ResumeGetResponse::fromResumeToResumeGetResponse))
                .meta(meta)
                .build();
    }

    @Override
    public ResultPaginationResponse fetchResumeByUser(Pageable pageable) {
        //query builder
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);

        Page<Resume> resumes = resumeRepository.findAll(spec, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(resumes.getTotalElements())
                .pages(resumes.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        return ResultPaginationResponse.builder()
                .result(resumes.getContent().stream().map(ResumeGetResponse::fromResumeToResumeGetResponse))
                .meta(meta)
                .build();
    }
}
