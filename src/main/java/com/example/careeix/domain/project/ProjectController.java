package com.example.careeix.domain.project;

import com.example.careeix.config.BaseException;
import com.example.careeix.config.BaseResponse;
import com.example.careeix.config.BaseResponseStatus;
import com.example.careeix.domain.project.dto.*;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectMapping;
import com.example.careeix.domain.project.service.ProjectService;
import com.example.careeix.domain.user.dto.InfoResponse;
import com.example.careeix.domain.user.service.UserService;
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

import javax.transaction.Transactional;
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
    private final UserService userService;

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
    @ApiOperation(value = "프로젝트 등록", notes =
            "end_date, projectNotes를 제외한 모든 값은 Mandatory입니다. \n" +
            "\n" +
            "단, is_proceed의 값이 (0 : 진행 종료)일 경우, end_date의 값 또한 Mandatory입니다. \n" +
            " is_proceed의 값이 (1 : 진행 중)일 경우, end_date의 값은 null로 저장됩니다. \n" +
            "\n" +
            " is_proceed의 default 값은 (0 : 진행 종료)입니다.\n"+
            "projectNotes가 입력 될 경우 projectNotes의 content는 Mandatory입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 response", response = PostProjectResponse.class),
            @ApiResponse(code = 400, message =
                    "2002 : 유효하지 않은 JWT입니다.\n" +
                    "2020 : 프로젝트 제목을 입력해주세요. \n" +
                    "2021 : 프로젝트 시작 날짜를 입력해주세요. \n" +
                    "2022 : 프로젝트 종료 날짜를 입력해주세요. \n" +
                    "2023 : 프로젝트 시작-종료 날짜를 순서에 맞게 입력해주세요. \n" +
                    "2024 : 프로젝트 구분을 입력해주세요.\n" +
                    "2025 : 프로젝트 소개를 입력해주세요.\n" +
                    "2026 : 프로젝트 내용 제목을 입력해주세요.\n" +
                    "2027 : 프로젝트 내용 본문을 입력해주세요.\n" +
                    "2028 : 프로젝트 메모 본문을 입력해주세요.\n" +
                    "2029 : 프로젝트 진행 여부 입력을 확인해주세요.(0 : 진행 종료, 1 : 진행 중)\n"
            ),
            @ApiResponse(code = 403, message = "J2002 : ACCESS-TOKEN이 맞지 않습니다."),
            @ApiResponse(code = 404, message = "J2001 : 헤더의 JWT 토큰이 비어있습니다.", response = ApiErrorResponse.class)
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

            //TODO
            // projectDetails 없을 때(projectDetail로 잘못 적었었음), 다른 key값들도 마찬가지.
            // 프로젝트는 생성되고 런타임error 생기는 오류.
            // 목차나 노트에서 오류가 날 경우 프로젝트도 저장 안되여야함.

            PostProjectResponse project = projectService.createProjectPackage(postProjectRequest, userId);

//            return new BaseResponse<>(postProjectResponse, SUCCESS_CREATE);
            return new ResponseEntity<>(new BaseResponse(project), SUCCESS.getHttpStatus());

        } catch (BaseException exception) {
            exception.printStackTrace();
//            return new BaseResponse<>((exception.getStatus()));
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }
    }

    /**
     * 프로젝트 조회(유저ID별) API
     * [GET] /by-user?id=
     *
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "프로젝트 조회(유저 ID별)", notes = "홈 > 카드 프로필 클릭 or 커리어 탭 랜딩에서 확인 가능한 프로젝트 리스트 입니다.\n" +
    "파라미터로 user의 id를 받으며, 필수로 입력해야합니다. (예. ~project/by-user?id=1)\n" +
    "순서는 start_date기준 내림차순입니다.(최근에 시작한 프로젝트일 수록 위)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 response", response = GetProjectResponse.class),
            @ApiResponse(code = 400, message =
                    "2011 : 파라미터 값(유저ID)을 입력해주세요\n" +
                    "U1003 : 해당 아이디를 찾을 수 없습니다.",response = ApiErrorResponse.class)
    })
    @ResponseBody
    @GetMapping("/by-user")
    public ResponseEntity<BaseResponse> getProjectsByUserId(@RequestParam(required = false) Long id){
        try {
//            Long userId = jwtService.getUserId();
//            // Validation
//            if (userId == null) {
//                return new ResponseEntity(new BaseResponse(INVALID_JWT),INVALID_JWT.getHttpStatus());
//            }


            // 유저 유무 확인
            if (id == null || userService.getUserByUserId(id) == null) {
                return new ResponseEntity(new BaseResponse(USER_PARAM_ERROR),USER_PARAM_ERROR.getHttpStatus());
            }

            List<GetProjectResponse> projectsByUserId = projectService.getProjectsByUserId(id);
            return new ResponseEntity<>(new BaseResponse(projectsByUserId), SUCCESS.getHttpStatus());


        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }

    }


    /**
     * 프로젝트 조회(프로젝트Id별) API
     * [GET] /:project_id
     *
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "프로젝트 조회(프로젝트 ID별)", notes = "프로젝트 ID에 해당하는 프로젝트, 목차(ProjectDeatils)리스트, 노트(ProjectNotes)리스트를 조회 할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 response", response = PostProjectResponse.class),
            @ApiResponse(code = 400, message =
                    "2031 : 존재하지 않는 프로젝트 ID입니다. 프로젝트 ID를 다시 확인해주세요\n" +
                    "2032 : 이미 삭제된 프로젝트 ID입니다. 프로젝트 ID를 다시 확인해주세요",response = ApiErrorResponse.class)
    })
    @ResponseBody
    @GetMapping("/{project_id}")
    public ResponseEntity<BaseResponse> getProjectByProjectId(@PathVariable("project_id") Long projectId){
        try {


            Optional<Project> project = projectService.getProjectById(projectId);
            // 프로젝트가 없는 경우
            if (project.isEmpty()) {
                return new ResponseEntity(new BaseResponse(INVALID_PROJECT),INVALID_PROJECT.getHttpStatus());
            }
            // 유저ID(by jwt) != 프로젝트의 유저ID
//            if (!project.get().getUser().getUserId().equals(userId)) {
//                return new ResponseEntity(new BaseResponse(INVALID_USER_JWT),INVALID_USER_JWT.getHttpStatus());
//            }
            // 삭제된 프로젝트 ID일 경우
            if (project.get().getStatus() == 0) {
                return new ResponseEntity(new BaseResponse(DELETED_PROJECT),DELETED_PROJECT.getHttpStatus());
            }


            PostProjectResponse projectResponse = projectService.getProjectByIdResponse(projectId);


            return new ResponseEntity<>(new BaseResponse(projectResponse), SUCCESS.getHttpStatus());


        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }

    }


    /**
     * 프로젝트 삭제 API
     * [PATCH] /:project_id
     *
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "프로젝트 삭제", notes = "project_id에 해당하는 프로젝트, 프로젝트 목차, 프로젝트 노트 들의 status를 모두 0으로 바꿉니다.\n" +
    "해당 프로젝트를 작성한 user의 JWT를 필수로 입력해야합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 response", response = String.class),
            @ApiResponse(code = 400, message =
                    "2002 : 유효하지 않은 JWT입니다.\n" +
                    "2003 : 권한이 없는 유저의 접근입니다.\n" +
                    "2031 : 존재하지 않는 프로젝트 ID입니다. 프로젝트 ID를 다시 확인해주세요\n" +
                    "2032 : 이미 삭제된 프로젝트 ID입니다. 프로젝트 ID를 다시 확인해주세요"),
            @ApiResponse(code = 403, message = "J2002 : ACCESS-TOKEN이 맞지 않습니다."),
            @ApiResponse(code = 404, message = "J2001 : 헤더의 JWT 토큰이 비어있습니다.", response = ApiErrorResponse.class)
    })
    @ResponseBody
    @PatchMapping("/{project_id}")
    public ResponseEntity<BaseResponse> deleteProject(@PathVariable("project_id") Long projectId){

        try {
            Long userId = jwtService.getUserId();
            if (userId == null) {
                return new ResponseEntity(new BaseResponse(INVALID_JWT),INVALID_JWT.getHttpStatus());
            }

            Optional<Project> project = projectService.getProjectById(projectId);
            // 프로젝트가 없는 경우
            if (project.isEmpty()) {
                return new ResponseEntity(new BaseResponse(INVALID_PROJECT),INVALID_PROJECT.getHttpStatus());
            }

            //유저 프로젝트 권한 확인
            if (project.get().getUser().getUserId() != userId) {
                return new ResponseEntity(new BaseResponse(INVALID_USER_JWT),INVALID_USER_JWT.getHttpStatus());
            }

            projectService.deleteProject(projectId);

            return new ResponseEntity<>(new BaseResponse(String.format("Project Id:%d이(가) 삭제 되었습니다.",projectId)), SUCCESS.getHttpStatus());


        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }

    }


    /**
     * 프로젝트 수정 API
     * [PATCH] /edit/:project_id
     *
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "프로젝트 수정", notes =
            "JWT는 Mandatory입니다.\n" +
            "수정하고자 하는 프로젝트의 Project_id를 입력 바랍니다.\n" +
            "end_date, projectNotes를 제외한 모든 값은 Mandatory입니다. \n" +
            "단, is_proceed의 값이 (0 : 진행 종료)일 경우, end_date의 값 또한 Mandatory입니다. \n" +
            " is_proceed의 값이 (1 : 진행 중)일 경우, end_date의 값은 null로 저장됩니다. \n" +
            " is_proceed의 default 값은 (0 : 진행 종료)입니다.\n"+
            "projectNotes가 입력 될 경우 projectNotes의 content는 Mandatory입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 response", response = PostProjectResponse.class),
            @ApiResponse(code = 400, message =
                    "2002 : 유효하지 않은 JWT입니다.\n" +
                    "2020 : 프로젝트 제목을 입력해주세요. \n" +
                    "2021 : 프로젝트 시작 날짜를 입력해주세요. \n" +
                    "2022 : 프로젝트 종료 날짜를 입력해주세요. \n" +
                    "2023 : 프로젝트 시작-종료 날짜를 순서에 맞게 입력해주세요. \n" +
                    "2024 : 프로젝트 구분을 입력해주세요.\n" +
                    "2025 : 프로젝트 소개를 입력해주세요.\n" +
                    "2026 : 프로젝트 내용 제목을 입력해주세요.\n" +
                    "2027 : 프로젝트 내용 본문을 입력해주세요.\n" +
                    "2028 : 프로젝트 메모 본문을 입력해주세요.\n" +
                    "2029 : 프로젝트 진행 여부 입력을 확인해주세요.(0 : 진행 종료, 1 : 진행 중)\n"
            ),
            @ApiResponse(code = 403, message = "J2002 : ACCESS-TOKEN이 맞지 않습니다."),
            @ApiResponse(code = 404, message = "J2001 : 헤더의 JWT 토큰이 비어있습니다.", response = ApiErrorResponse.class)
    })
    @ResponseBody
    @PatchMapping("/edit/{project_id}")
    public ResponseEntity<BaseResponse> editProject(@PathVariable("project_id") Long projectId, @RequestBody @Valid PostProjectRequest postProjectRequest){
        //FIXME
        // 프로젝트는 save로 덮어씌움
        // projectDetails, projectNotes 는 지웠다가 새로 만들어서 저장 -> 이게 맞나..? 효율성 측면에서 확인하고 필요시 수정
        // 위 처럼 진행한 이유는
        // 1. projectDetails, projectNotes의 순서가 바뀌는 경우가 존재 -> 각 id 오름차순으로 출력되는 상황이어서 save로 덮어 씌울 경우, 순서가 적용 안됨. -> created 순으로 출력하면 되려나?
        // 2. 새로 추가되는 경우가 존재 -> 생각해보니까 save로 해결되네. (save는 이미 있으면 덮어 씌우고 없으면 새로 생성)

        try {
            Long userId = jwtService.getUserId();

            // Validation
            if (userId == null) {
//                return new BaseResponse<>(INVALID_JWT);
                return new ResponseEntity(new BaseResponse(INVALID_JWT),INVALID_JWT.getHttpStatus());
            }

            // projectId의 해당 프로젝트
            Optional<Project> projectById = projectService.getProjectById(projectId);

            // 프로젝트가 없는 경우
            if (projectById.isEmpty()) {
                return new ResponseEntity(new BaseResponse(INVALID_PROJECT),INVALID_PROJECT.getHttpStatus());
            }
            //유저 프로젝트 권한 확인
            if (projectById.get().getUser().getUserId() != userId) {
                return new ResponseEntity(new BaseResponse(INVALID_USER_JWT),INVALID_USER_JWT.getHttpStatus());
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


            PostProjectResponse projectEdited = projectService.editProjectPackage(postProjectRequest, projectById.get());


//            return new BaseResponse<>(postProjectResponse, SUCCESS_CREATE);
            return new ResponseEntity<>(new BaseResponse(projectEdited), SUCCESS.getHttpStatus());



        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }

    }



}

