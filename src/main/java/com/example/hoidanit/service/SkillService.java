package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.SkillRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.model.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface SkillService {
    Skill getSkill(long id);
    Skill createSkill(SkillRequestDTO skillRequestDTO);
    Skill updateSkill(long id, SkillRequestDTO skillRequestDTO);
    void deleteSkill(long id);
    ResultPaginationResponse getSkills(Specification<Skill> specification, Pageable pageable);
}
