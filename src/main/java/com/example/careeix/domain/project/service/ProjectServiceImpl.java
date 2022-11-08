package com.example.careeix.domain.project.service;

import com.example.careeix.config.BaseException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PostProjectResponse createProjectPackage(PostProjectRequest postProjectRequest, Long user_id) throws BaseException {

        try{
            User user = userRepository.findByUserId(user_id).get();
            //project DB에 저장
            Project projectSaved = createProject(postProjectRequest.toEntity(user));

            List<PostProjectDetail> pdSaved = new ArrayList<>();

            for (PostProjectDetail pd : postProjectRequest.getProjectDetails()) {
                if (pd.getProject_detail_title() == null) {
                    throw new BaseException(EMPTY_PDETAIL_TITLE);
                }
                if (pd.getContent() == null) {
                    throw new BaseException(EMPTY_PDETAIL_CONTENT);
                }
                //project Detail DB에 저장
                ProjectDetail projectDetailSaved = createProjectDetail(pd, projectSaved);


                if(pd.getProjectNotes() != null) {
                    for (PostProjectNote pn : pd.getProjectNotes()) {
                        if (pn.getContent() == null) {
                            throw new BaseException(EMPTY_PNOTE_CONTENT);
                        }
                        //project Note DB에 저장
                        createProjectNote(pn, projectDetailSaved);

                    }
                }
                pdSaved.add(pd);
            }

            PostProjectResponse postProjectResponse = new PostProjectResponse(
                    projectSaved.getProjectId(),
                    projectSaved.getTitle(),
                    projectSaved.getStartDate(),
                    projectSaved.getEndDate(),
                    projectSaved.getIsProceed(),
                    projectSaved.getClassification(),
                    projectSaved.getIntroduction(),
                    pdSaved
            );

            return postProjectResponse;

        } catch (BaseException exception) {
            exception.printStackTrace();
            if (exception.getStatus()==EMPTY_PDETAIL_TITLE) {
                throw new BaseException(EMPTY_PDETAIL_TITLE);
            } else if (exception.getStatus() == EMPTY_PDETAIL_CONTENT) {
                throw new BaseException(EMPTY_PDETAIL_CONTENT);
            } else if (exception.getStatus() == EMPTY_PNOTE_CONTENT) {
                throw new BaseException(EMPTY_PNOTE_CONTENT);
            } else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
    }

    @Override
    public Project createProject(Project project) throws BaseException {
        try{
            return projectRepository.save(project);
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

    @Transactional
    @Override
    public PostProjectResponse editProjectPackage(PostProjectRequest postProjectRequest, Project project) throws BaseException {
        try{
            Project projectSaved = editPostProject(postProjectRequest, project);
            projectRepository.save(projectSaved);

            for (ProjectDetail pd : projectSaved.getProjectDetails()) {
                projectNoteRepository.deleteAllByProjectDetail_ProjectDetailId(pd.getProjectDetailId());
            }
            projectDetailRepository.deleteAllByProject_ProjectId(projectSaved.getProjectId());

            List<PostProjectDetail> pdSaved = new ArrayList<>();

            for (PostProjectDetail pd : postProjectRequest.getProjectDetails()) {
                if (pd.getProject_detail_title() == null) {
                    throw new BaseException(EMPTY_PDETAIL_TITLE);
                }
                if (pd.getContent() == null) {
                    throw new BaseException(EMPTY_PDETAIL_CONTENT);
                }
                //project Detail DB에 저장
                ProjectDetail projectDetailSaved = createProjectDetail(pd, projectSaved);

                //FIXME
                // 🚨 projectNotes는 Mandatory가 아님..!
                for (PostProjectNote pn : pd.getProjectNotes()) {
                    if (pn.getContent() == null) {
                        throw new BaseException(EMPTY_PNOTE_CONTENT);
                    }
                    //project Note DB에 저장
                    createProjectNote(pn, projectDetailSaved);

                }
                pdSaved.add(pd);
            }

            PostProjectResponse postProjectResponse = new PostProjectResponse(
                    projectSaved.getProjectId(),
                    projectSaved.getTitle(),
                    projectSaved.getStartDate(),
                    projectSaved.getEndDate(),
                    projectSaved.getIsProceed(),
                    projectSaved.getClassification(),
                    projectSaved.getIntroduction(),
                    pdSaved
            );

            return postProjectResponse;

        } catch (BaseException exception) {
            exception.printStackTrace();
            if (exception.getStatus()==EMPTY_PDETAIL_TITLE) {
                throw new BaseException(EMPTY_PDETAIL_TITLE);
            } else if (exception.getStatus() == EMPTY_PDETAIL_CONTENT) {
                throw new BaseException(EMPTY_PDETAIL_CONTENT);
            } else if (exception.getStatus() == EMPTY_PNOTE_CONTENT) {
                throw new BaseException(EMPTY_PNOTE_CONTENT);
            } else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
    }

    @Override
    public Project editPostProject(PostProjectRequest postProjectRequest, Project project) throws BaseException {
        try {
            project.setClassification(postProjectRequest.getClassification());
            project.setEndDate(postProjectRequest.getEnd_date());
            project.setIntroduction(postProjectRequest.getIntroduction());
            project.setIsProceed(postProjectRequest.getIs_proceed());
            project.setStartDate(postProjectRequest.getStart_date());
            project.setTitle(postProjectRequest.getTitle());
            //FIXME 무식한 방법인거 같은데.... builder는 어떻게 사용하는거지??

            return project;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Override
    public ProjectDetail editProjectDetail(PostProjectDetail postProjectDetail, ProjectDetail projectDetail) throws BaseException {
        try {
            projectDetail.setContent(postProjectDetail.getContent());
            projectDetail.setProjectDetailTitle(postProjectDetail.getProject_detail_title());
            //FIXME 무식한 방법인거 같은데.... builder는 어떻게 사용하는거지??

            return projectDetail;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public ProjectNote editProjectNote(PostProjectNote postProjectNote, ProjectNote projectNote) throws BaseException {
        try {
            projectNote.setContent(postProjectNote.getContent());
            //FIXME 무식한 방법인거 같은데.... builder는 어떻게 사용하는거지??

            return projectNote;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

