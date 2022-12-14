package com.example.careeix.domain.job.repository;

import com.example.careeix.domain.job.entity.Job;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.repository.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface JobRepository extends JpaRepository<Job, Long>{

    Optional<Job> findByJobId(Long jobId);

    List<Job> findByJobName(String jobName);



}


