package com.example.careeix.domain.project.repository;

import com.example.careeix.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository  extends JpaRepository<Project, Long> {


}
