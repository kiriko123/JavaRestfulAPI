package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.SkillRequestDTO;
import com.example.hoidanit.model.Skill;
import com.example.hoidanit.service.SkillService;
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
@RequestMapping("/api/v1/skills")
@Validated
@Slf4j
public class SkillController {

    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<?> addSkill(@Valid @RequestBody SkillRequestDTO skillRequestDTO) {
        log.info("Create skill request: {}", skillRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createSkill(skillRequestDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(@Valid @RequestBody SkillRequestDTO skillRequestDTO, @Min(1)@PathVariable long id) {
        log.info("Update skill request: {}", skillRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(skillService.updateSkill(id, skillRequestDTO));
    }
    @GetMapping
    public ResponseEntity<?> getAllSkills(@Filter Specification<Skill> specification,
                                          Pageable pageable) {
        log.info("Get all skills");
        return ResponseEntity.ok().body(skillService.getSkills(specification, pageable));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable long id) {
        log.info("Delete skill request: {}", id);
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
