package com.example.careeix.domain.project.repository;

import com.example.careeix.domain.project.entity.ProjectNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectNoteRepository extends JpaRepository<ProjectNote, Long> {
}
