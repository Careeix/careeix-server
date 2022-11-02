package com.example.careeix.domain.project.service;

import com.example.careeix.config.BaseException;
import com.example.careeix.domain.project.dto.*;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project createProject(@Valid PostProjectRequest postProjectRequest, Long userId) throws BaseException;

    ProjectDetail createProjectDetail(@Valid PostProjectDetail projectDetail, Project project) throws BaseException;

//    void createProjectNotes(@Valid List<PostProjectNote> projectNotes, ProjectDetail projectDetail);

    void createProjectNote(@Valid PostProjectNote projectNote, ProjectDetail projectDetail) throws BaseException;

    Optional<Project> getProjectById(long projectId);
    String getProjectDetail(long projectId);
    List<String> getProjectNote(long projectId);

    List<GetProjectResponse> getProjectsByUserId(long userId) throws BaseException;

    void deleteProject(Long projectId) throws BaseException;

}
