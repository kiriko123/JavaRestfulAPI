package com.example.hoidanit.repository;

import com.example.hoidanit.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepository extends JpaRepository<Job, Long> , JpaSpecificationExecutor<Job> {
}
