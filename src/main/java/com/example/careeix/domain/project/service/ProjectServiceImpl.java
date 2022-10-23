//package com.example.careeix.domain.project.service;
//
//import com.example.careeix.domain.project.dto.PostProjectDetail;
//import com.example.careeix.domain.project.dto.PostProjectRequest;
//import com.example.careeix.domain.project.dto.PostProjectResponse;
//import com.example.careeix.domain.project.entity.Project;
//import com.example.careeix.domain.project.entity.ProjectDetail;
//import com.example.careeix.domain.project.entity.ProjectNote;
//import com.example.careeix.domain.project.repository.ProjectDetailRepository;
//import com.example.careeix.domain.project.repository.ProjectNoteRepository;
//import com.example.careeix.domain.project.repository.ProjectRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProjectServiceImpl implements ProjectService {
//
//    private final ProjectRepository projectRepository;
//    private final ProjectDetailRepository projectDetailRepository;
//    private final ProjectNoteRepository projectNoteRepository;
//
//
//    @Override
//    public PostProjectResponse createProject(PostProjectRequest postProjectRequest, Long user_id) {
//
//        Long userId = user_id;
//        Project project = postProjectRequest.toEntity();
//        Project projectSaved = projectRepository.save(project);
//
//
//        return projectRepository.save(project);
//    }
//
//    @Override
//    public void createProjectDetail(List<PostProjectDetail> projectDetails, Project project) {
//        for (PostProjectDetail pd : projectDetails) {
//            ProjectDetail projectDetail = projectDetailRepository.save();
//
//        }
////        ProjectDetail projectDetailCreated = projectDetailRepository.save(projectDetail);
////        return projectDetailCreated;
//    }
//
//    @Override
//    public ProjectNote createProjectNote(ProjectNote projectNote) {
//        ProjectNote projectNoteCreated = projectNoteRepository.save(projectNote);
//        return projectNoteCreated;
//    }
//}
