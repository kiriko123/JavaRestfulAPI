package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.resume.ResumeCreateRequestDTO;
import com.example.hoidanit.dto.request.resume.ResumeUpdateRequestDTO;
import com.example.hoidanit.model.Resume;
import com.example.hoidanit.service.ResumeService;
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
@RequestMapping("/api/v1/resumes")
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<?> addResume(@Valid @RequestBody ResumeCreateRequestDTO resumeCreateRequestDTO) {
        log.info("Add resume request: {}", resumeCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.createResume(resumeCreateRequestDTO));
    }

    @PutMapping
    public ResponseEntity<?> updateResume(@Valid @RequestBody ResumeUpdateRequestDTO resumeUpdateRequestDTO) {
        log.info("Update resume request: {}", resumeUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resumeService.updateResume(resumeUpdateRequestDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getResume( @Min(1)@PathVariable Long id) {
        log.info("Get resume request: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.getOne(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(@Min(1)@PathVariable Long id) {
        log.info("Delete resume request: {}", id);
        resumeService.deleteResume(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<?> getResumes(@Filter Specification<Resume> specification, Pageable pageable) {
        log.info("Get all");
        return ResponseEntity.ok().body(resumeService.getAll(specification, pageable));
    }
    @PostMapping("/by-user")
    public ResponseEntity<?> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok().body(resumeService.fetchResumeByUser(pageable));
    }
}
