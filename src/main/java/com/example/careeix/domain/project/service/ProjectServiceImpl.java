package com.example.careeix.domain.project.service;

import com.example.careeix.config.BaseException;
import com.example.careeix.config.BaseResponse;
import com.example.careeix.domain.project.dto.*;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;
import com.example.careeix.domain.project.repository.ProjectDetailRepository;
import com.example.careeix.domain.project.repository.ProjectNoteRepository;
import com.example.careeix.domain.project.repository.ProjectRepository;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.careeix.config.BaseResponseStatus.*;

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
    public GetSelectProjectResponse getProjectByIdResponse(long projectId) throws BaseException {
        try {
            Project p = projectRepository.findById(projectId).get();

            List<String> pdTitleList = new ArrayList<>();
            List<ProjectDetail> pd = projectDetailRepository.findAllByProject_ProjectId(p.getProjectId());

            for (ProjectDetail x : pd) {
                pdTitleList.add(x.getProjectDetailTitle());
            }

            if (p.getStatus() == 1) {
//                return new GetProjectResponse(p.getProjectId(), p.getTitle(), p.getStartDate(), p.getEndDate(), p.getIsProceed(), p.getClassification(), p.getIntroduction());
                return new GetSelectProjectResponse(p.getProjectId(), p.getTitle(), p.getStartDate(), p.getEndDate(), p.getIsProceed(), p.getClassification(), pdTitleList);
            } else {
                throw new BaseException(PROJECT_STATUS_ERROR);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Override
    public String getProjectDetail(long projectId) {
        return null;
    }

    @Override
    public List<String> getProjectNote(long projectId) {
        return null;
    }

    @Override

    public List<GetProjectResponse> getProjectsByUserId(long userId) throws BaseException {
        try {
            List<GetProjectResponse> getProjectResponseList = new ArrayList<>();
//            List<Project> projectsByUser_userId = projectRepository.findProjectsByUser_UserId(userId);
            List<Project> projectsByUser_userId = projectRepository.findAllByUser_UserId(userId);

            for (Project p : projectsByUser_userId) {
                if (p.getStatus() == 1) {
                    GetProjectResponse getProjectResponse = new GetProjectResponse(p.getProjectId(), p.getTitle(), p.getStartDate(), p.getEndDate(), p.getIsProceed(), p.getClassification(), p.getIntroduction());
                    getProjectResponseList.add(getProjectResponse);
                } else {
                    continue;
                }

            }

            return getProjectResponseList;

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public void deleteProject(Long projectId) throws BaseException {
        try {
//            Project project = getProjectById(projectId).get();
            Project project = projectRepository.findById(projectId).get();

            for(ProjectDetail pd : project.getProjectDetails()){
                deleteProjectNotes(pd.getProjectDetailId());
            }

            deleteProjectDetails(projectId);

            //이미 삭제한 프로젝일 경우
            if (project.getStatus() == 0) {
                throw new BaseException(DELETED_PROJECT);
            }
            else{
                project.setStatus(0);
                projectRepository.save(project);
            }


        } catch (BaseException exception) {
            exception.printStackTrace();
            if (exception.getStatus()==DELETED_PROJECT) {
                throw new BaseException(DELETED_PROJECT);
            } else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
    }

    @Override
    public void deleteProjectDetails(Long projectId) throws BaseException {
        try {
            List<ProjectDetail> projectDetailList = projectDetailRepository.findAllByProject_ProjectId(projectId);

            for (ProjectDetail pd : projectDetailList) {
                pd.setStatus(0);
                projectDetailRepository.save(pd);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public void deleteProjectNotes(Long projectDetailId) throws BaseException {
        try {
            List<ProjectNote> projectNoteList = projectNoteRepository.findAllByProjectDetail_ProjectDetailId(projectDetailId);

            for (ProjectNote pn : projectNoteList) {
                pn.setStatus(0);
                projectNoteRepository.save(pn);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Override
    public Optional<Project> getProjectById(long projectId) throws BaseException {
        try {
            return projectRepository.findById(projectId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
