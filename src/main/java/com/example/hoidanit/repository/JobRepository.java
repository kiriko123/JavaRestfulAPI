package com.example.hoidanit.repository;

import com.example.hoidanit.model.Company;
import com.example.hoidanit.model.Job;
import com.example.hoidanit.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> , JpaSpecificationExecutor<Job> {

    List<Job> findBySkillsIn(List<Skill> skills);

}
