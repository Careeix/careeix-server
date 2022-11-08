package com.example.careeix.domain.project.repository;

import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectDetailMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Long> {

    @Query("select pd from ProjectDetail pd where pd.project.projectId = ?1 and pd.status=1")
    List<ProjectDetail> findAllByProject_ProjectIdAndStatus(Long projectId, int status);


    void deleteAllByProject_ProjectId(Long projectId);

    List<ProjectDetailMapping> findAllByProject_ProjectId(Long projectId);

//    <T> List<T> findAllByProject_ProjectId(Long projectId, Class<T> type);

}
