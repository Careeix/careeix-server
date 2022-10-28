package com.example.careeix.domain.project.service;

import com.example.careeix.config.BaseException;
import com.example.careeix.domain.project.dto.PostProjectDetail;
import com.example.careeix.domain.project.dto.PostProjectNote;
import com.example.careeix.domain.project.dto.PostProjectRequest;
import com.example.careeix.domain.project.dto.PostProjectResponse;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;
import com.example.careeix.domain.project.repository.ProjectDetailRepository;
import com.example.careeix.domain.project.repository.ProjectNoteRepository;
import com.example.careeix.domain.project.repository.ProjectRepository;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.careeix.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final ProjectNoteRepository projectNoteRepository;
    private final UserRepository userRepository;


    @Override
    public Project createProject(PostProjectRequest postProjectRequest, Long user_id) throws BaseException {

        try{
            User user = userRepository.findByUserId(user_id).get();
            Project projectSaved = projectRepository.save(postProjectRequest.toEntity(user));

            return projectSaved;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public ProjectDetail createProjectDetail(PostProjectDetail projectDetail, Project project) throws BaseException {
        try{
            return projectDetailRepository.save(projectDetail.toEntity(project));
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    @Override
//    public void createProjectNotes(List<PostProjectNote> projectNotes, ProjectDetail projectDetail) {
//        for (PostProjectNote pn : projectNotes) {
//            projectNoteRepository.save(pn.toEntity(projectDetail));
//        }
//    }

    @Override
    public void createProjectNote(PostProjectNote projectNote, ProjectDetail projectDetail) throws BaseException {
        try{
            projectNoteRepository.save(projectNote.toEntity(projectDetail));
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public Optional<Project> getProjectById(long projectId) {
        return projectRepository.findById(projectId);
    }

    @Override
    public String getProjectDetail(long projectId) {
        return null;
    }

    @Override
    public List<String> getProjectNote(long projectId) {
        return null;
    }
}
