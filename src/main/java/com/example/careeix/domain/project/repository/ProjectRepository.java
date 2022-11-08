package com.example.careeix.domain.project.repository;

import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public interface ProjectRepository  extends JpaRepository<Project, Long> {



//    @Query("select p from Project p where p.user.userId = ?1 and p.status=1 order by p.startDate DESC")
//    List<Project> findProjectsByUser_UserId(Long userId);

    @Query("select p from Project p where p.user.userId = ?1 and p.status=1 order by p.startDate DESC")
    List<Project> findAllByUser_UserId(Long userId);

//    @Query("select User from Project p where p.projectId = ?1 and p.user.status=1")
//    User findUserByProjectId(Long projectId);

}
