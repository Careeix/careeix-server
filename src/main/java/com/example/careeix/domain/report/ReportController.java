package com.example.careeix.domain.report;

import com.example.careeix.config.BaseException;
import com.example.careeix.config.BaseResponse;
import com.example.careeix.domain.project.dto.PostProjectResponse;
import com.example.careeix.domain.report.dto.ReportUserResponse;
import com.example.careeix.domain.report.service.ReportService;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.service.UserService;
import com.example.careeix.utils.exception.ApiErrorResponse;
import com.example.careeix.utils.jwt.service.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.careeix.config.BaseResponseStatus.*;

@RestController
@RequestMapping("api/v1/report")
@RequiredArgsConstructor
@Api(tags = "Report API")
public class ReportController {

    private final JwtService jwtService;
    private final ReportService reportService;
    private final UserService userService;

    /**
     * 신고 등록/취소 API
     * [POST] ?id=
     *
     * @return ResponseEntity<BaseResponse>
     */

    @ApiOperation(value = "신고 접수 및 취소", notes = "Header에는 신고자의 JWT를,\n"+
                                                    "파라미터로 피신고자(신고를 당하는 유저)의 id를 입력합니다.\n" +
                                                    "이미 신고자가 동일한 피신고자를 신고할 경우, 신고가 취소됩니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 response", response = ReportUserResponse.class),
            @ApiResponse(code = 400, message =
                            "2011 : 파라미터 값(유저ID)을 입력해주세요\n" +
                            "2002 : 유효하지 않은 JWT입니다.\n" +
                            "2033 : 신고자ID와 피신고자ID가 동일합니다.\n" +
                            "U1003 : 해당 아이디를 찾을 수 없습니다."
            ),
            @ApiResponse(code = 403, message = "J2002 : ACCESS-TOKEN이 맞지 않습니다."),
            @ApiResponse(code = 404, message = "J2001 : 헤더의 JWT 토큰이 비어있습니다.", response = ApiErrorResponse.class)
    })
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<BaseResponse> reportUser(@RequestParam(value = "id",required = false) Long reportUserToId) {
        try {

            if (reportUserToId == null || userService.getUserByUserId(reportUserToId) == null) {
                return new ResponseEntity(new BaseResponse(USER_PARAM_ERROR),USER_PARAM_ERROR.getHttpStatus());
            }

            Long reportUserFromId = jwtService.getUserId();

            if (reportUserFromId == reportUserToId) {
                return new ResponseEntity(new BaseResponse(REPORT_ID_EQUAL),REPORT_ID_EQUAL.getHttpStatus());
            }

            // User Validation
            if (reportUserFromId == null) {
                return new ResponseEntity(new BaseResponse(INVALID_JWT),INVALID_JWT.getHttpStatus());
            }

            ReportUserResponse reportUserResponse = reportService.reportUser(reportUserToId, reportUserFromId);

            return new ResponseEntity<>(new BaseResponse(reportUserResponse), SUCCESS.getHttpStatus());

        } catch (BaseException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(exception.getStatus()),exception.getStatus().getHttpStatus());
        }
    }



}
