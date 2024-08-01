package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.JobRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.model.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface JobService {
    Job createJob(JobRequestDTO jobRequestDTO);
    Job updateJob(Long id, JobRequestDTO jobRequestDTO);
    void deleteJob(Long id);
    Job getJob(Long id);
    ResultPaginationResponse getJobs(Specification<Job> specification, Pageable pageable);
}
