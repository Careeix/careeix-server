package com.example.careeix.domain.project;

import com.example.careeix.config.BaseException;
import com.example.careeix.config.BaseResponse;
import com.example.careeix.config.BaseResponseStatus;
import com.example.careeix.domain.project.dto.*;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.service.ProjectService;
import com.example.careeix.utils.dto.ApplicationResponse;
import com.example.careeix.utils.jwt.service.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.careeix.config.BaseResponseStatus.*;

@RestController
@RequestMapping("api/v1/project")
@RequiredArgsConstructor
@Api(tags = "Project API")
public class ProjectController {

    private final JwtService jwtService;
    private final ProjectService projectService;

    //날짜
    public int compareDates(String start, String end) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int result = 1;

        try {
            Date sd = simpleDateFormat.parse(start);
            Date ed = simpleDateFormat.parse(end);
            result = sd.compareTo(ed);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 등록 API
     * [POST] /create
     *
     * @return BaseResponse<PostProjectResponse>
     */
    @ApiOperation(value = "프로젝트 등록")
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProjectResponse> createProject(@RequestBody @Valid PostProjectRequest postProjectRequest){
        try{
            Long userId = jwtService.getUserId();

            // Validation
            if (userId == null) {
                return new BaseResponse<>(INVALID_JWT);
            }
            if (postProjectRequest.getTitle() == null) {
                return new BaseResponse<>(EMPTY_TITLE);
            }
            if (postProjectRequest.getStart_date() == null) {
                return new BaseResponse<>(EMPTY_START_DATE);
            }
            // 종료 날짜 X && 진행 종료
            if (postProjectRequest.getEnd_date() == null && postProjectRequest.getIs_proceed() == 0) {
                return new BaseResponse<>(EMPTY_END_DATE);
            }
            // 시작 날짜가 종료 날짜 보다 클 경우
            if (postProjectRequest.getIs_proceed() == 0 && compareDates(postProjectRequest.getStart_date(), postProjectRequest.getEnd_date()) > 0) {
                return new BaseResponse<>(DISORDERED_DATE);
            }
            if (postProjectRequest.getIs_proceed() != 0 && postProjectRequest.getIs_proceed() != 1) {
                return new BaseResponse<>(INVALID_ISPROCEED);
            }
            if (postProjectRequest.getClassification() == null) {
                return new BaseResponse<>(EMPTY_CLASSIFICATION);
            }
            if (postProjectRequest.getIntroduction() == null) {
                return new BaseResponse<>(EMPTY_INTRODUCTION);
            }


            Project projectSaved = projectService.createProject(postProjectRequest, userId);
            Long projectId = projectSaved.getProjectId();

            List<PostProjectDetail> pdSaved = new ArrayList<>();

            for (PostProjectDetail pd : postProjectRequest.getProjectDetails()) {
                if (pd.getProject_detail_title() == null) {
                    return new BaseResponse<>(EMPTY_PDETAIL_TITLE);
                }
                if (pd.getContent() == null) {
                    return new BaseResponse<>(EMPTY_PDETAIL_CONTENT);
                }

                ProjectDetail projectDetailSaved = projectService.createProjectDetail(pd, projectSaved);

                for (PostProjectNote pn : pd.getProjectNotes()) {
                    if (pn.getContent() == null) {
                        return new BaseResponse<>(EMPTY_PNOTE_CONTENT);
                    }
                    projectService.createProjectNote(pn, projectDetailSaved);
                }
                pdSaved.add(pd);
            }

            PostProjectResponse postProjectResponse = new PostProjectResponse(
                    projectId,
                    projectSaved.getTitle(),
                    projectSaved.getStartDate(),
                    projectSaved.getEndDate(),
                    projectSaved.getIsProceed(),
                    projectSaved.getClassification(),
                    projectSaved.getIntroduction(),
                    pdSaved
            );

            return new BaseResponse<>(postProjectResponse);

        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}

