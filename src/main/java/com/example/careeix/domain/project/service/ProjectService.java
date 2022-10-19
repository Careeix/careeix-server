package com.example.careeix.domain.project.service;

import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;

public interface ProjectService {

    Project createProject(Project project);

    ProjectDetail createProjectDetail(ProjectDetail projectDetail);

    ProjectNote createProjectNote(ProjectNote projectNote);
}
