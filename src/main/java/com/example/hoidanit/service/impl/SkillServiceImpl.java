package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.SkillRequestDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.exception.InvalidDataException;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Skill;
import com.example.hoidanit.repository.SkillRepository;
import com.example.hoidanit.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public Skill getSkill(long id) {
        return skillRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
    }

    @Override
    public Skill createSkill(SkillRequestDTO skillRequestDTO) {
        Skill skill = skillRepository.findByName(skillRequestDTO.getName());
        if (skill != null) {
            throw new InvalidDataException("Skill name already exists");
        }

        return skillRepository.save(Skill.builder().name(skillRequestDTO.getName()).build());
    }

    @Override
    public Skill updateSkill(long id, SkillRequestDTO skillRequestDTO) {
        Skill skill = getSkill(id);

        String name = skillRequestDTO.getName();
        if(skillRepository.findByName(name) != null) {
            throw new InvalidDataException("Skill name already exists");
        }
        skill.setName(name);
        return skillRepository.save(skill);
    }

    @Override
    public void deleteSkill(long id) {

    }

    @Override
    public ResultPaginationResponse getSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> skills = skillRepository.findAll(specification, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(skills.getTotalElements())
                .pages(skills.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        return ResultPaginationResponse.builder()
                .result(skills.getContent())
                .meta(meta)
                .build();
    }
}
