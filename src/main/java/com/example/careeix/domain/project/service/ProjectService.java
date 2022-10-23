package com.example.careeix.domain.project.service;

import com.example.careeix.domain.project.dto.PostProjectDetail;
import com.example.careeix.domain.project.dto.PostProjectRequest;
import com.example.careeix.domain.project.dto.PostProjectResponse;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectNote;

import javax.validation.Valid;
import java.util.List;

public interface ProjectService {

    PostProjectResponse createProject(@Valid PostProjectRequest postProjectRequest, Long userId);

    void createProjectDetail(@Valid List<PostProjectDetail> projectDetail);

    ProjectNote createProjectNote(@Valid ProjectNote projectNote);
}
