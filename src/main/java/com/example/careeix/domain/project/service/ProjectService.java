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

    PostProjectResponse createProjectPackage(@Valid PostProjectRequest postProjectRequest, Long userId) throws BaseException;


    Project createProject(@Valid Project project) throws BaseException;
    ProjectDetail createProjectDetail(@Valid PostProjectDetail projectDetail, Project project) throws BaseException;
    void createProjectNote(@Valid PostProjectNote projectNote, ProjectDetail projectDetail) throws BaseException;

    PostProjectResponse getProjectByIdResponse(long projectId) throws BaseException;
    String getProjectDetail(long projectId);
    List<String> getProjectNote(long projectId);

    List<GetProjectResponse> getProjectsByUserId(long userId) throws BaseException;

    void deleteProject(Long projectId) throws BaseException;
    void deleteProjectDetails(Long projectId) throws BaseException;
    void deleteProjectNotes(Long projectDetailId) throws BaseException;

    //TODO
    // 유저 탈퇴시 모든 프로젝트/프로젝트디테일/프로젝트노트 status=0으로
    void deleteAllProjetsByUserId(Long userId) throws BaseException;

    Optional<Project> getProjectById (long projectId) throws BaseException;

//파라미터 수정
    PostProjectResponse editProjectPackage(@Valid PostProjectRequest postProjectRequest, Project project) throws BaseException;
    Project editPostProject(@Valid PostProjectRequest postProjectRequest, Project project) throws BaseException;
    ProjectDetail editProjectDetail(@Valid PostProjectDetail postProjectDetail, ProjectDetail projectDetail) throws BaseException;
    ProjectNote editProjectNote(@Valid PostProjectNote postProjectNote, ProjectNote projectNote) throws BaseException;
}
