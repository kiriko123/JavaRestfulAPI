package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.resume.ResumeCreateRequestDTO;
import com.example.hoidanit.dto.request.resume.ResumeUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.dto.response.resume.ResumeCreateResponse;
import com.example.hoidanit.dto.response.resume.ResumeGetResponse;
import com.example.hoidanit.dto.response.resume.ResumeUpdateResponse;
import com.example.hoidanit.model.Resume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ResumeService {
    ResumeCreateResponse createResume(ResumeCreateRequestDTO resumeCreateRequestDTO);
    ResumeUpdateResponse updateResume(ResumeUpdateRequestDTO resumeUpdateRequestDTO);
    void deleteResume(long id);
    //List<Resume> getResumes();
    Resume getResume(long id);
    ResumeGetResponse getOne(long id);
    ResultPaginationResponse getAll(Specification<Resume> specification, Pageable pageable);
}
