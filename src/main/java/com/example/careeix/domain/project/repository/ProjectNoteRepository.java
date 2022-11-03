package com.example.careeix.domain.project.repository;

import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectNoteRepository extends JpaRepository<ProjectNote, Long> {

    @Query("select pn from ProjectNote pn where pn.projectDetail.projectDetailId = ?1 and pn.status=1")
    List<ProjectNote> findAllByProjectDetail_ProjectDetailId(Long projectDetailId);
}
