package com.example.careeix.domain.user.controller;


import com.example.careeix.config.BaseException;
import com.example.careeix.domain.color.service.ColorService;
import com.example.careeix.domain.user.dto.*;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.NotFoundUserException;
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
@Api(tags = "User API - 혜도")
public class UserController {

    private final UserService userService;

    private final ColorService colorService;
    private final UserJobService userJobService;
    private final JwtService jwtService;
    private final OAuth2UserServiceKakao oAuth2UserServiceKakao;

//
//    /**
//     * 닉네임 중복확인
//     * @param nicknameDuplicateRequest - valid
//     * @return ResponseEntity<MessageResponse>
//     */
//    @ApiOperation(value = "닉네임 중복확인", notes = "닉네임 중복 여부 체크, 2~10글자 validation")
//    @ApiResponses(value = {
//            @ApiResponse(code = 400 , message = "회원의 닉네임을 입력해주세요. \t\n 닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다."),
//            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다.", response = UserNicknameDuplicateException.class)
//    })
//    @PostMapping("/check-nickname")
//    public ApplicationResponse<MessageResponse> duplicateCheckUser(@Valid @RequestBody NicknameDuplicateRequest nicknameDuplicateRequest) {
//
//        userService.userNicknameDuplicateCheck(nicknameDuplicateRequest.getUserNickname());
//
//        return ApplicationResponse.ok(MessageResponse.builder()
//                .message("사용가능한 닉네임입니다.")
//                .build());
//    }


    /**
     * 사용자 정보 조회
     * @param
     * @return
     */
    @ApiOperation(value = "사용자 정보 조회", notes = "사용자 정보를 조회합니다. 사용자의 모든 정보를 포함했습니다. 마이페이지나 다른 유저 정보를 조회할때" +
            "쓰시면 됩니다. userWork, userSocial(0: 카카오, 1: 구글) : int, userId : long")
    @GetMapping("/profile/{userId}")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "해당 아이디를 찾을 수 없습니다.")
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
    @ApiOperation(value = "사용자 프로필 수정  - jwt 0", notes = "사용자 프로필을 수정합니다. 이미지는 multipart 형식을 이용해서 주셔야 됩니다. " +
            "null로 보내도 허용하게 했습니다. s3서버를 이용하여 저장하고 불러오고 있습니다. 저장정보 주소를 풀로 저장하고 있기때문에 불러오고 저장할때 추가로 작업하실건 없습니다.", produces = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "회원의 닉네임을 입력해주세요. \t\n 닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다."),
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다."),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 만료되었습니다."),
            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다."),
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
    @ApiOperation(value = "사용자 정보 수정  - jwt 0", notes = "사용자 정보를 수정합니다. 사용자 정보 수정 페이지입니다. jwt로 사용자 판별하고 " +
            "사용자의 reqeust정보들을 고칩니다. 닉네임, 사진 고치는 건 프로필 수정 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다."),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 만료되었습니다.")
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
    @ApiOperation(value = "사용자 추천 프로필 - jwt 0, 개발 완료됐으나 더미데이터 추가후 테스트 필요, 하는중", notes = "사용자의 직무에 관련된 프로필 리스트를 조회합니다." +
            "사용자의 직무랑 사용자의 세부 직무를 다른 사람들 직무랑 비교해서 찾아서 주고 있습니다. 6개 limit을 걸어두었고 리스트엔 userId를 반환하니까" +
            "클릭하게 되면 이 userID를 이용하셔서 상세 조회하시면 됩니다. 그땐 유저 상세 조회 api를 이용해주세요")
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
    @ApiOperation(value = "사용자 로그아웃 - note 참조", notes = "클라이언트에서 일했을 때 클라에서 이런걸 다했었는데 혹시 필요하시면 쓰시라고 해뒀습니다." +
            "굳이 안쓰셔도 되면 냅두시면 됩니다.")
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
    @ApiOperation(value = "회원 탈퇴 - jwt 0", notes = "회원 탈퇴를 합니다. 회원 탈퇴시 기본 status 1을 0으로 바꿉니다.")
    @PostMapping("/withdraw")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다."),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 만료되었습니다."),
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
    @ApiOperation(value = "카카오 로그인 - 첫번째 호출", notes = "userId 0이나 jwt null이면 추가정보 받는 kakao-login api 이용," +
            "카카오 엑세스 토큰을 통해 검증 작업후 상황별 아래 에러코드들 반환합니다. int userWork, userSocial(0: 카카오, 1: 구글), long userId")
    @PostMapping("/check-login")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "카카오 로그인에 실패했습니다."),
            @ApiResponse(code = 401 , message = "카카오 인증에 실패했습니다."),
            @ApiResponse(code = 405 , message = "카카오의 지정된 요청 방식 이외의 프로토콜을 전달했습니다."),
            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다."),
            @ApiResponse(code = 500 , message = "카카오 API URL이 잘못되었습니다."),
            @ApiResponse(code = 500 , message = "카카오 API 응답을 읽는데 실패했습니다."),
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
    @ApiOperation(value = "카카오 로그인 - 추가정보 입력 후 호출", notes = "회원가입 후 로그인 - 추가 정보 받고 호출하는 api, 연차 0,1,2,3 으로 전달해주세요" +
            "위와 같은 카카오 api를 이용하고 있는데 위에서 검증을 하고 넘기기 때문에 그부분에 대한 에러코드는 생략했습니다." +
            "userWork, userSocial(0: 카카오, 1: 구글) : int, userId : long")
    @PostMapping("/kakao-login")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "회원의 닉네임을 입력해주세요. \t\n 닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다."),
            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다.")
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
                .message("정보를 불러오는데 성공하였습니다.")
                .build());
    }





}