package com.example.careeix.domain.project.service;

import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;
import com.example.careeix.domain.project.repository.ProjectDetailRepository;
import com.example.careeix.domain.project.repository.ProjectNoteRepository;
import com.example.careeix.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final ProjectNoteRepository projectNoteRepository;


    @Override
    public Project createProject(Project project) {
        Project projectCreated = projectRepository.save(project);
        return projectCreated;
    }

    @Override
    public ProjectDetail createProjectDetail(ProjectDetail projectDetail) {
        ProjectDetail projectDetailCreated = projectDetailRepository.save(projectDetail);
        return projectDetailCreated;
    }

    @Override
    public ProjectNote createProjectNote(ProjectNote projectNote) {
        ProjectNote projectNoteCreated = projectNoteRepository.save(projectNote);
        return projectNoteCreated;
    }
}
