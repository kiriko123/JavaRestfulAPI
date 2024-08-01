package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.JobRequestDTO;
import com.example.hoidanit.dto.response.JobResponseDTO;
import com.example.hoidanit.dto.response.ResultPaginationResponse;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Job;
import com.example.hoidanit.model.Skill;
import com.example.hoidanit.repository.JobRepository;
import com.example.hoidanit.repository.SkillRepository;
import com.example.hoidanit.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    @Override
    public Job createJob(JobRequestDTO jobRequestDTO) {

        List<JobRequestDTO.Skill> skills = jobRequestDTO.getSkills();
        List<Skill> skillList = listSkillDtoToListSkill(skills);

        Job job = Job.builder()
                .name(jobRequestDTO.getName())
                .description(jobRequestDTO.getDescription())
                .active(jobRequestDTO.isActive())
                .location(jobRequestDTO.getLocation())
                .levelEnum(jobRequestDTO.getLevel())
                .salary(jobRequestDTO.getSalary())
                .startDate(jobRequestDTO.getStartDate())
                .endDate(jobRequestDTO.getEndDate())
                .location(jobRequestDTO.getLocation())
                .skills(skillList)
                .build();

        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, JobRequestDTO jobRequestDTO) {
        List<JobRequestDTO.Skill> skills = jobRequestDTO.getSkills();
        List<Skill> skillList = listSkillDtoToListSkill(skills);

        Job job = getJob(id);
        job.setName(jobRequestDTO.getName());
        job.setDescription(jobRequestDTO.getDescription());
        job.setActive(jobRequestDTO.isActive());
        job.setLocation(jobRequestDTO.getLocation());
        job.setLevelEnum(jobRequestDTO.getLevel());
        job.setSalary(jobRequestDTO.getSalary());
        job.setStartDate(jobRequestDTO.getStartDate());
        job.setEndDate(jobRequestDTO.getEndDate());
        job.setLocation(jobRequestDTO.getLocation());

        /*if (!skillList.isEmpty()){
            job.setSkills(skillList);
        }*/
        job.setSkills(skillList);
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        getJob(id);
        jobRepository.deleteById(id);
    }

    @Override
    public Job getJob(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }

    @Override
    public ResultPaginationResponse getJobs(Specification<Job> specification, Pageable pageable) {
        Page<Job> jobs = jobRepository.findAll(specification, pageable);

        ResultPaginationResponse.Meta meta = ResultPaginationResponse.Meta.builder()
                .total(jobs.getTotalElements())
                .pages(jobs.getTotalPages())
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(jobs.getContent().stream().map(JobResponseDTO::fromJobToJobResponse))
                .build();
    }

    private List<Skill> listSkillDtoToListSkill(List<JobRequestDTO.Skill> skills){
        List<Skill> skillList = new ArrayList<>();
        for (JobRequestDTO.Skill skill : skills) {
            if (skillRepository.existsById(skill.getId())){
                skillList.add(skillRepository.findById(skill.getId()).orElse(null));
            }
            /*else{
                throw new ResourceNotFoundException("Skill with id:= " + skill.getId() +" Not Found");
            }*/
        }
        return skillList;
    }
}
