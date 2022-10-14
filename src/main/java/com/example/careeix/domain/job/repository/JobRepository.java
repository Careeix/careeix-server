package com.example.careeix.domain.job.repository;

import com.example.careeix.domain.job.entity.Job;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.repository.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JobRepository extends JpaRepository<Job, Long>, UserRepositoryCustom {

    Optional<Job> findByJobId(Long jobId);

    Job findByJobName(String jobName);



}


