package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.resume.ResumeCreateRequestDTO;
import com.example.hoidanit.dto.request.resume.ResumeUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.dto.response.resume.ResumeCreateResponse;
import com.example.hoidanit.dto.response.resume.ResumeGetResponse;
import com.example.hoidanit.dto.response.resume.ResumeUpdateResponse;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Resume;
import com.example.hoidanit.repository.JobRepository;
import com.example.hoidanit.repository.ResumeRepository;
import com.example.hoidanit.repository.UserRepository;
import com.example.hoidanit.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public ResumeCreateResponse createResume(ResumeCreateRequestDTO resumeCreateRequestDTO) {

        //check job
        if(!jobRepository.existsById(resumeCreateRequestDTO.getJob().getId())){
            throw new ResourceNotFoundException("Job not found with id = " + resumeCreateRequestDTO.getJob().getId());
        }
        //check user
        if (!userRepository.existsById(resumeCreateRequestDTO.getUser().getId())){
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

        Page<Resume> resumes = resumeRepository.findAll(specification, pageable);

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
