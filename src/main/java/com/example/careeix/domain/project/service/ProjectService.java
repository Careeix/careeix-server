package com.example.careeix.domain.project.service;

import com.example.careeix.domain.project.dto.PostProjectDetail;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectNote;

import javax.validation.Valid;
import java.util.List;

public interface ProjectService {

    Project createProject(@Valid Project project);

    void createProjectDetail(@Valid List<PostProjectDetail> projectDetail);

    ProjectNote createProjectNote(@Valid ProjectNote projectNote);
}
