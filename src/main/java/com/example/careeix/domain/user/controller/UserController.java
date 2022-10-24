package com.example.careeix.domain.user.controller;


import com.example.careeix.config.BaseException;
import com.example.careeix.domain.color.service.ColorService;
import com.example.careeix.domain.user.dto.*;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.UserNicknameDuplicateException;
import com.example.careeix.domain.user.exception.oauth2.kakao.*;
import com.example.careeix.domain.user.service.OAuth2UserServiceKakao;
import com.example.careeix.domain.user.service.UserJobService;
import com.example.careeix.domain.user.service.UserService;
import com.example.careeix.utils.dto.ApplicationResponse;
import com.example.careeix.utils.jwt.exception.ExpireAccessException;
import com.example.careeix.utils.jwt.exception.NotFoundJwtException;
import com.example.careeix.utils.jwt.service.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    private final ColorService colorService;
    private final UserJobService userJobService;
    private final JwtService jwtService;
    private final OAuth2UserServiceKakao oAuth2UserServiceKakao;


    /**
     * 닉네임 중복확인
     * @param nicknameDuplicateRequest - valid
     * @return ResponseEntity<MessageResponse>
     */
    @ApiOperation(value = "닉네임 중복확인", notes = "닉네임 중복 여부 체크, 2~10글자 validation")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "회원의 닉네임을 입력해주세요. \t\n 닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다."),
            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다.", response = UserNicknameDuplicateException.class)
    })
    @PostMapping("/check-nickname")
    public ApplicationResponse<MessageResponse> duplicateCheckUser(@Valid @RequestBody NicknameDuplicateRequest nicknameDuplicateRequest) {

        userService.userNicknameDuplicateCheck(nicknameDuplicateRequest.getUserNickname());

        return ApplicationResponse.ok(MessageResponse.builder()
                .message("사용가능한 닉네임입니다.")
                .build());
    }


    /**
     * 사용자 정보 조회
     * @param
     * @return
     */
    @ApiOperation(value = "사용자 정보 조회", notes = "사용자 정보를 조회합니다.")
    @GetMapping("/profile/{userId}")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.", response = NotFoundJwtException.class),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 만료되었습니다.", response = ExpireAccessException.class),
    })
    public ApplicationResponse<LoginResponse> getUserProfile(@PathVariable long userId) {
        User user = userService.getUserByUserId(userId);
        return getLoginResponseResponseEntity(user);
    }



    /**
     * 사용자 프로필 수정
     * @param userProfileRequest, file
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "사용자 프로필 수정  - jwt 0", notes = "사용자 프로필을 수정합니다.", produces = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "회원의 닉네임을 입력해주세요. \t\n 닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다."),
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.", response = NotFoundJwtException.class),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 만료되었습니다.", response = ExpireAccessException.class),
            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다.", response = UserNicknameDuplicateException.class),
    })
    @PostMapping("/update-profile")
    public ApplicationResponse<MessageResponse> updateUserProfile(@Valid @ModelAttribute UserProfileRequest userProfileRequest,
                                                    @RequestParam(required = false) MultipartFile file) {

        long userId = jwtService.getUserId();

        User user = userService.updateUserProfile(userId, userProfileRequest.getUserNickName(), file);

        return ApplicationResponse.ok(MessageResponse.builder()
                .message("사용자 프로필이 수정되었습니다.")
                .build());
    }


    /**
     * 사용자 정보 수정
     * @param userInfoRequest
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "사용자 정보 수정  - jwt 0", notes = "사용자 정보를 수정합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.", response = NotFoundJwtException.class),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 만료되었습니다.", response = ExpireAccessException.class)
    })
    @PostMapping("/update-info")
    public ApplicationResponse<MessageResponse> updateUserInfo(@Valid @ModelAttribute UserInfoRequest userInfoRequest) {

        long userId = jwtService.getUserId();
        User user = userService.updateUserInfo(userId, userInfoRequest);
        userJobService.updateUserJob(user, userInfoRequest.getUserDetailJob());

        return ApplicationResponse.ok(MessageResponse.builder()
                .message("사용자 정보가 수정되었습니다.")
                .build());
    }

    /**
     * 사용자 직무에 관련된 프로필 리스트
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "사용자 추천 프로필 - jwt 0, 개발중", notes = "사용자의 직무에 관련된 프로필 리스트를 조회합니다." +
            "사용자의 직무랑 사용자의 세부 직무를 다른 사람들 직무랑 비교해서 찾아서 주기")
    @GetMapping("/recommend/profile")
    public ApplicationResponse<List<ProfileRecommendResponse>> getRecommendProfile() {
        long userId = jwtService.getUserId();
        User user = userService.getUserByUserId(userId);
        List<ProfileRecommendResponse> profileRecommendResponses = userJobService.getProfile(user);

        return ApplicationResponse.ok(profileRecommendResponses);
    }



    /**
     * 사용자 로그아웃
     * @param request
     * @return ResponseEntity<MessageResponse>
     */
    @ApiOperation(value = "사용자 로그아웃", notes = "사용자 로그아웃을 합니다.")
    @GetMapping("/logout")
    public ApplicationResponse<MessageResponse> logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return ApplicationResponse.ok(MessageResponse.builder()
                .message("로그아웃에 성공했습니다.")
                .build());
    }


    /**
     * 회원 탈퇴
     * @return ResponseEntity<MessageResponse>
     */
    @ApiOperation(value = "회원 탈퇴 - jwt 0", notes = "회원 탈퇴를 합니다.")
    @PostMapping("/withdraw")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.", response = NotFoundJwtException.class),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 만료되었습니다.", response = ExpireAccessException.class),
    })
    public ApplicationResponse<MessageResponse> withdrawUser() {
        long userId = jwtService.getUserId();
        userService.withdrawUser(userId);

        return ApplicationResponse.ok(MessageResponse.builder()
                .message("회원 탈퇴가 완료되었습니다.")
                .build());
    }



    /**
     * OAuth2
     */

    /**
     * 카카오 로그인 API
     * [POST] api/v1/users/check-login
     * @return ResponseEntity
    \     */
    @ApiOperation(value = "카카오 로그인", notes = "첫번째 호출 - userId 0이나 jwt null이면 추가정보 받고 kakao-login api로")
    @PostMapping("/check-login")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "카카오 로그인에 실패했습니다."),
    })
    public ApplicationResponse<LoginResponse> checkKakaoUser(@Valid @RequestBody KakaoAccessRequest kakaoAccessRequest) {
        User user = oAuth2UserServiceKakao.validateKakaoAccessToken(kakaoAccessRequest.getAccessToken());
//        if(user.getSocialId() == null){
//            throw new KakaoFailException();
//        }
        // 회원가입 한 적 없는 경우 - 첫번째 호출
        if (user.getUserJob() == null) {
            return ApplicationResponse.ok(LoginResponse.builder()
                    .message("회원가입을 진행해주세요")
                    .build());
        }
        return getLoginResponseResponseEntity(user);
    }


    /**
     * 카카오 로그인 API
     * [POST] api/v1/users/kakao-login
     * @param kakaoLoginRequest
     * @return ResponseEntity
\     */
    @ApiOperation(value = "카카오 로그인", notes = "회원가입 후 로그인 - 추가 정보 받고 호출하는 api, 연차 0,1,2,3 으로 전달해주세요")
    @PostMapping("/kakao-login")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "카카오 로그인에 실패했습니다."),
            @ApiResponse(code = 400 , message = "회원의 닉네임을 입력해주세요. \t\n 닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다."),
            @ApiResponse(code = 401 , message = "카카오 인증에 실패했습니다."),
            @ApiResponse(code = 405 , message = "카카오의 지정된 요청 방식 이외의 프로토콜을 전달했습니다."),
            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다."),
            @ApiResponse(code = 500 , message = "카카오 API URL이 잘못되었습니다."),
            @ApiResponse(code = 500 , message = "카카오 API 응답을 읽는데 실패했습니다."),
    })
    public ApplicationResponse<LoginResponse> loginKakaoUser(@Valid @RequestBody KakaoLoginRequest kakaoLoginRequest) {
        User user = oAuth2UserServiceKakao.validateKakaoAccessToken(kakaoLoginRequest.getAccessToken());
        if(user.getSocialId() == null){
            throw new KakaoFailException();
        }

        if(!Objects.equals(user.getUserNickName(), kakaoLoginRequest.getNickname())){
            userService.userNicknameDuplicateCheck(kakaoLoginRequest.getNickname());

        }

        // 회원가입 한 적 없는 경우 - 데이터 저장
        try {
            User finalUser = userService.insertUser(kakaoLoginRequest, user);
            userJobService.createUserJob(kakaoLoginRequest.getUserDetailJob(), finalUser);
            User u = colorService.getColorName(finalUser);
            return getLoginResponseResponseEntity(u);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // 로그인 정보 불러오기
    }

    private ApplicationResponse<LoginResponse> getLoginResponseResponseEntity(User user) {
        List<String> userJobList = userJobService.getUserJobName(user.getUserId());
        String jwt = jwtService.createJwt(user.getUserId());
        return ApplicationResponse.ok(LoginResponse.builder()
                .userId(user.getUserId())
                .userWork(user.getUserWork())
                .userIntro(user.getIntoContent())
                .userProfileImg(user.getUserProfileImg())
                .userProfileColor(user.getUserProfileColor())
                .userSocialProvider(user.getUserSocialProvider())
                .userDetailJobs(userJobList)
                .userNickname(user.getUserNickName())
                .userEmail(user.getUserEmail())
                .userJob(user.getUserJob())
                .jwt(jwt)
                .message("로그인에 성공하였습니다.")
                .build());
    }





}