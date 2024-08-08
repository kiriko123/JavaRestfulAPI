package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.JobRequestDTO;
import com.example.hoidanit.dto.response.JobResponseDTO;
import com.example.hoidanit.model.Job;
import com.example.hoidanit.service.JobService;
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
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/jobs")
@Slf4j
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<?> addJob(@Validated @RequestBody JobRequestDTO jobRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(JobResponseDTO.fromJobToJobResponse(jobService.createJob(jobRequestDTO)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@Validated @RequestBody JobRequestDTO jobRequestDTO, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(JobResponseDTO.fromJobToJobResponse(jobService.updateJob(id, jobRequestDTO)));
    }
    @GetMapping
    public ResponseEntity<?> getAllJobs(@Filter Specification<Job> specification, Pageable pageable) {
        return ResponseEntity.ok().body(jobService.getJobs(specification, pageable));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@Min(1) @PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@Min(1) @PathVariable Long id) {
        return ResponseEntity.ok().body(jobService.getJob(id));
    }
}
