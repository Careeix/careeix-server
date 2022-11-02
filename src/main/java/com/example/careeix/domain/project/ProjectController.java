package com.example.careeix.domain.project;

import com.example.careeix.config.BaseException;
import com.example.careeix.config.BaseResponse;
import com.example.careeix.config.BaseResponseStatus;
import com.example.careeix.domain.project.dto.*;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.service.ProjectService;
import com.example.careeix.utils.dto.ApplicationResponse;
import com.example.careeix.utils.exception.ApiErrorResponse;
import com.example.careeix.utils.jwt.exception.ExpireAccessException;
import com.example.careeix.utils.jwt.exception.JwtException;
import com.example.careeix.utils.jwt.exception.NotFoundJwtException;
import com.example.careeix.utils.jwt.service.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
     * 프로젝트 등록 API
     * [POST] /
     *
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "프로젝트 등록", notes = "User JWT값은 (사용자 정보 조회 : /api/v1/users/profile/{userId}) 참고해주시면 감사하겠습니다. \n" +
            "end_date을 제외한 모든 값은 Mandatory입니다. \n" +
            "\n" +
            "단, is_proceed의 값이 (0 : 진행 종료)일 경우, end_date의 값 또한 Mandatory입니다. \n" +
            "is_proceed의 값이 (1 : 진행 중)일 경우, end_date의 값은 null로 저장됩니다. \n" +
            "\n" +
            "is_proceed의 default 값은 (0 : 진행 종료)입니다.")
    @ApiResponses({
            @ApiResponse(code = 403, message = "헤더의 JWT 토큰이 맞지 않거나 만료되었습니다."),
            @ApiResponse(code = 404, message = "헤더의 JWT 토큰이 비어있습니다.", response = ApiErrorResponse.class)
    })
    @ResponseBody
    @PostMapping("")
//    public BaseResponse<PostProjectResponse> createProject(@RequestBody @Valid PostProjectRequest postProjectRequest){
    public ResponseEntity<BaseResponse> createProject(@RequestBody @Valid PostProjectRequest postProjectRequest){
        try{
            Long userId = jwtService.getUserId();

            // Validation
            if (userId == null) {
//                return new BaseResponse<>(INVALID_JWT);
                return new ResponseEntity(new BaseResponse(INVALID_JWT),INVALID_JWT.getHttpStatus());
            }
            if (postProjectRequest.getTitle() == null) {
//                return new BaseResponse<>(EMPTY_TITLE);
                return new ResponseEntity(new BaseResponse(EMPTY_TITLE),EMPTY_TITLE.getHttpStatus());
            }
            if (postProjectRequest.getStart_date() == null) {
//                return new BaseResponse<>(EMPTY_START_DATE);
                return new ResponseEntity(new BaseResponse(EMPTY_START_DATE),EMPTY_START_DATE.getHttpStatus());
            }
            // 종료 날짜 X && 진행 종료
            if (postProjectRequest.getEnd_date() == null && postProjectRequest.getIs_proceed() == 0) {
//                return new BaseResponse<>(EMPTY_END_DATE);
                return new ResponseEntity(new BaseResponse(EMPTY_END_DATE),EMPTY_END_DATE.getHttpStatus());
            }
            // 시작 날짜가 종료 날짜 보다 클 경우
            if (postProjectRequest.getIs_proceed() == 0 && compareDates(postProjectRequest.getStart_date(), postProjectRequest.getEnd_date()) > 0) {
//                return new BaseResponse<>(DISORDERED_DATE);
                return new ResponseEntity(new BaseResponse(DISORDERED_DATE),DISORDERED_DATE.getHttpStatus());
            }
            if (postProjectRequest.getIs_proceed() != 0 && postProjectRequest.getIs_proceed() != 1) {
//                return new BaseResponse<>(INVALID_ISPROCEED);
                return new ResponseEntity(new BaseResponse(INVALID_ISPROCEED),INVALID_ISPROCEED.getHttpStatus());
            }
            if (postProjectRequest.getClassification() == null) {
//                return new BaseResponse<>(EMPTY_CLASSIFICATION);
                return new ResponseEntity(new BaseResponse(EMPTY_CLASSIFICATION),EMPTY_CLASSIFICATION.getHttpStatus());
            }
            if (postProjectRequest.getIntroduction() == null) {
//                return new BaseResponse<>(EMPTY_INTRODUCTION);
                return new ResponseEntity(new BaseResponse(EMPTY_INTRODUCTION),EMPTY_INTRODUCTION.getHttpStatus());
            }
            // 진행 중 일 경우, end_date = null로 set
            if (postProjectRequest.getIs_proceed()==1) {
                postProjectRequest.setEnd_date(null);
            }


            Project projectSaved = projectService.createProject(postProjectRequest, userId);
            Long projectId = projectSaved.getProjectId();

            List<PostProjectDetail> pdSaved = new ArrayList<>();

            for (PostProjectDetail pd : postProjectRequest.getProjectDetails()) {
                if (pd.getProject_detail_title() == null) {
//                    return new BaseResponse<>(EMPTY_PDETAIL_TITLE);
                    return new ResponseEntity(new BaseResponse(EMPTY_PDETAIL_TITLE),EMPTY_PDETAIL_TITLE.getHttpStatus());
                }
                if (pd.getContent() == null) {
//                    return new BaseResponse<>(EMPTY_PDETAIL_CONTENT);
                    return new ResponseEntity(new BaseResponse(EMPTY_PDETAIL_CONTENT),EMPTY_PDETAIL_CONTENT.getHttpStatus());
                }

                ProjectDetail projectDetailSaved = projectService.createProjectDetail(pd, projectSaved);

                for (PostProjectNote pn : pd.getProjectNotes()) {
                    if (pn.getContent() == null) {
//                        return new BaseResponse<>(EMPTY_PNOTE_CONTENT);
                        return new ResponseEntity(new BaseResponse(EMPTY_PNOTE_CONTENT),EMPTY_PNOTE_CONTENT.getHttpStatus());
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

//            return new BaseResponse<>(postProjectResponse, SUCCESS_CREATE);
            return new ResponseEntity<>(new BaseResponse(postProjectResponse), SUCCESS.getHttpStatus());

        } catch (BaseException exception) {
            exception.printStackTrace();
//            return new BaseResponse<>((exception.getStatus()));
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }
    }

    /**
     * 프로젝트 조회 API
     * [GET] /
     *
     * @return ResponseEntity<BaseResponse>
     */
    @ResponseBody
    @GetMapping("")
    public ResponseEntity<BaseResponse> getProjectsByUserId(){
        try {
            Long userId = jwtService.getUserId();

            if (userId == null) {
                return new ResponseEntity(new BaseResponse(INVALID_JWT),INVALID_JWT.getHttpStatus());
            }

            List<GetProjectResponse> projectsByUserId = projectService.getProjectsByUserId(userId);
            return new ResponseEntity<>(new BaseResponse(projectsByUserId), SUCCESS.getHttpStatus());


        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }

    }

    @ResponseBody
    @PatchMapping("/{project_id}")
    public ResponseEntity<BaseResponse> deleteProject(@PathVariable("project_id") Long projectId){

        try {
            Long userId = jwtService.getUserId();
            if (userId == null) {
                return new ResponseEntity(new BaseResponse(INVALID_JWT),INVALID_JWT.getHttpStatus());
            }

            //유저 프로젝트 권한 확인
            Project project = projectService.getProjectById(projectId).get();
            if (project.getUser().getUserId() != userId) {
                return new ResponseEntity(new BaseResponse(INVALID_USER_JWT),INVALID_USER_JWT.getHttpStatus());
            }

            projectService.deleteProject(projectId);

            return new ResponseEntity<>(new BaseResponse(String.format("Project Id:%d이(가) 삭제 되었습니다.",projectId)), SUCCESS.getHttpStatus());


        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }

    }

    @ResponseBody
    @PatchMapping("/{project_id}/edit")
    public ResponseEntity<BaseResponse> editProject(@PathVariable("project_id") Long projectId){
        try {

        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }

    }



}

