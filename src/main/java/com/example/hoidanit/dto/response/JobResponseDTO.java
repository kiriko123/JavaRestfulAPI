package com.example.hoidanit.dto.response;

import com.example.hoidanit.model.Job;
import com.example.hoidanit.model.Skill;
import com.example.hoidanit.util.constant.LevelEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class JobResponseDTO implements Serializable {

    private long id;

    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;

    private boolean active;

    private List<String> skills;

    public static JobResponseDTO fromJobToJobResponse(Job job) {
        List<Skill> skillsInJob = job.getSkills();
        List<String> skillStringList = new ArrayList<>();
        if(skillsInJob != null) {
            for (Skill skill : skillsInJob) {
                skillStringList.add(skill.getName());
            }
        }

        return JobResponseDTO.builder()
                .id(job.getId())
                .name(job.getName())
                .location(job.getLocation())
                .salary(job.getSalary())
                .quantity(job.getQuantity())
                .active(job.isActive())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .description(job.getDescription())
                .level(job.getLevelEnum())
                .skills(skillStringList)
                .build();
    }
}
