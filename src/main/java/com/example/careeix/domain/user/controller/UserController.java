package com.example.careeix.domain.user.controller;


import com.example.careeix.config.BaseException;
import com.example.careeix.domain.color.service.ColorService;
import com.example.careeix.domain.project.service.ProjectService;
import com.example.careeix.domain.user.dto.*;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.*;
import com.example.careeix.domain.user.exception.oauth2.apple.AppleFailException;
import com.example.careeix.domain.user.exception.oauth2.kakao.*;
import com.example.careeix.domain.user.service.*;
import com.example.careeix.utils.dto.ApplicationResponse;
import com.example.careeix.utils.exception.ApiErrorResponse;
import com.example.careeix.utils.exception.ApplicationException;
import com.example.careeix.utils.jwt.exception.ExpireAccessException;
import com.example.careeix.utils.jwt.exception.NotFoundJwtException;
import com.example.careeix.utils.jwt.service.JwtService;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.io.IOException;
import java.util.*;

import static com.example.careeix.utils.ValidationRegex.isRegexNickname;
import static com.example.careeix.utils.ValidationRegex.isRegexNicknameNum;


@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Api(tags = "User API - 혜도")
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

    private final ColorService colorService;
    private final UserJobService userJobService;

    private final AppleServiceImpl appleService;
    private final JwtService jwtService;
    private final OAuth2UserServiceKakao oAuth2UserServiceKakao;

    private final OAuth2UserServiceApple oAuth2UserServiceApple;


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
    @ApiOperation(value = "사용자 정보 조회", notes = "사용자 정보를 조회합니다. \t\n 마이페이지나 다른 유저 정보를 조회할때 사용" +
            "\t\n userDetailJobs : [상세직무 리스트]" +"\n" +
            "           userId : long,유저 아이디 \n" +
            "       userIntro : 유저 소개글 \n" +
            "        userJob : 유저 직무 \n" +
            "         userNickname : 유저 닉네임 \n" +
            "          userProfileColor : 유저 프로필 색상 \n" +
            "           userProfileImg : 유저 프로필 이미지 \n" +
            "            userSocialProvider : int (0: 카카오, 1: 구글) 가입 경로 \n" +
            "           userWork : 유저 년차 int(0,1,2,3)", response = ApiErrorResponse.class)
    @GetMapping("/profile/{userId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "200 response", response = InfoResponse.class),
            @ApiResponse(code = 400 , message = "해당 아이디를 찾을 수 없습니다.(U1003)")
    })
    public ApplicationResponse<InfoResponse> getUserProfile(@PathVariable long userId) {
        User user = userService.getUserByUserId(userId);
        String jwt = jwtService.createJwt(user.getUserId());
        System.out.println(jwt);
        return getInfoResponseResponseEntity(user);
    }



    /**
     * 사용자 프로필 수정
     * @param userNickname
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "사용자 프로필 수정 (닉네임)  - jwt 0", notes = "사용자 프로필을 수정합니다."+
            "\t\n userNickname : 유저 닉네임")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "유효하지 않은 닉네임 입니다.(U1007) \t\n JWT 토큰이 비어있습니다.(J2001)"),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 맞지 않습니다.(J2002)"),
            @ApiResponse(code = 409, message = "중복된 닉네임 입니다.(U1001) \t\n 기존 닉네임과 동일합니다(U1006)", response = ApiErrorResponse.class),
    })
    @PostMapping("/update-profile-nickname")
    public ApplicationResponse<MessageResponse> updateUserProfile(@RequestParam String userNickname) {
        if (!isRegexNickname(userNickname)) {
            throw new UserNicknameValidException();
        }
        if (isRegexNicknameNum(userNickname)) {
            throw new UserNicknameValidException();
        }
        long userId = jwtService.getUserId();

        User user = userService.updateUserProfileNickname(userId, userNickname);


        return ApplicationResponse.ok(MessageResponse.builder()
                .message("사용자 프로필이 수정되었습니다.")
                .build());
    }

    /**
     * 사용자 프로필 수정
     * @param  file
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "사용자 프로필 수정 (프로필 이미지)  - jwt 0", notes = "사용자 프로필을 수정합니다. \t\n 이미지파일: multipartfile 타입 이용, null 허용" +
            "\t\n 저장정보 s3 주소를 풀로 저장하고 있기때문에 불러오고 저장할때 추가로 작업하실건 없습니다." +
            "\t\n file : 이미지 파일(유저프로필이미지)", produces = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.(J2001)"),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 맞지 않습니다.(J2002)", response = ApiErrorResponse.class),

    })
    @PostMapping("/update-profile-file")
    public ApplicationResponse<ProfileImageModifyResponse> updateUserProfileFile(
                                                                  @RequestParam(required = false) MultipartFile file) {

        long userId = jwtService.getUserId();

        User user = userService.updateUserProfileFile(userId, file);


        return ApplicationResponse.ok(ProfileImageModifyResponse.builder()
                .userProfileImg(user.getUserProfileImg())
                .message("사용자 프로필이 수정되었습니다.")
                .build());
    }

    @ApiOperation(value = "사용자 프로필 삭제 (프로필 이미지)  - jwt 0", notes = "사용자 프로필을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.(J2001)"),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 맞지 않습니다.(J2002)", response = ApiErrorResponse.class),

    })
    @PostMapping("/delete-profile-file")
    public ApplicationResponse<ProfileImageModifyResponse> deleteUserProfileFile(
    ) {

        long userId = jwtService.getUserId();

        User user = userService.deleteUserProfileFile(userId);


        return ApplicationResponse.ok(ProfileImageModifyResponse.builder()
                .userProfileImg(user.getUserProfileImg())
                .message("사용자 프로필이 삭제되었습니다.")
                .build());
    }

    /**
     * 사용자 정보 수정
     * @param userInfoRequest
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "사용자 정보 수정  - jwt 0", notes = "사용자 정보를 수정합니다. 사용자 정보 수정 페이지입니다. \t\n jwt로 사용자 판별하고 " +
            "사용자의 reqeust정보들을 고칩니다. " +
            "\t\n requestBody 소개글(userIntro)null 허용, 나머진 필수값(세부직무 1~3개, 년차(0~3)), 중복된 세부 직무 여부 체크"+
            "\t\n 닉네임, 사진 수정: 프로필 수정 api." +
            "\t\n userDetailJobs : [상세직무 리스트]" +"\n" +
            "       userIntro : 유저 소개글 \n" +
            "        userJob : 유저 직무 \n" +
            "           userWork : 유저 년차 int(0,1,2,3))")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.(J2001)"),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 맞지 않습니다.(J2002)"),
            @ApiResponse(code = 409, message = "중복된 세부직무가 있습니다.(U1005)", response = ApiErrorResponse.class)
    })
    @JsonProperty
    @PostMapping("/update-info")
    public ApplicationResponse<MessageResponse> updateUserInfo(@Valid @RequestBody UserInfoRequest userInfoRequest) {
        this.checkDuplicateJob(userInfoRequest.getUserDetailJob());

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
    @ApiOperation(value = "사용자 추천 프로필 - jwt 0", notes = "사용자의 직무에 관련된 프로필 리스트를 조회합니다." +
            "\t\n 사용자의 직무와 세부 직무를 다른 사람들 직무랑 비교해서 찾아서 주고 있습니다. 6개 limit을 걸어두었고 리스트엔 userId를 반환하니까" +
            "\t\n 클릭하게 되면 이 userID를 이용하셔서 상세 조회하시면 됩니다. 그땐 유저 상세 조회 api를 이용해주세요. 데이터가 없으면 null 리턴하고 있습니다." ,response = ApiErrorResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "200 response, list반환", response = ProfileRecommendResponse.class),
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.(J2001)"),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 맞지 않습니다.(J2002)", response = ApiErrorResponse.class),
    })
    @GetMapping("/recommend/profile")
    public ApplicationResponse<List<ProfileRecommendResponse>> getRecommendProfile() {
        long userId = jwtService.getUserId();
        User user = userService.getUserByUserId(userId);
        List<ProfileRecommendResponse> profileRecommendResponses = userJobService.getProfile(user);

        return ApplicationResponse.ok(profileRecommendResponses);
    }



//    /**
//     * 사용자 로그아웃
//     * @param request
//     * @return ResponseEntity<MessageResponse>
//     */
//    @ApiOperation(value = "사용자 로그아웃 - note 참조", notes = "클라이언트에서 일했을 때 클라에서 이런걸 다했었는데 혹시 필요하시면 쓰시라고 해뒀습니다." +
//            "굳이 안쓰셔도 되면 냅두시면 됩니다.", response = ApiErrorResponse.class)
//    @GetMapping("/logout")
//    public ApplicationResponse<MessageResponse> logoutUser(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.invalidate();
//
//        return ApplicationResponse.ok(MessageResponse.builder()
//                .message("로그아웃에 성공했습니다.")
//                .build());
//    }


    /**
     * 회원 탈퇴
     * @return ResponseEntity<MessageResponse>
     */
    @ApiOperation(value = "회원 탈퇴 - jwt 0", notes = "회원 탈퇴를 합니다. 회원 탈퇴시 기본 status 1을 0으로 바꿉니다.")
    @PostMapping("/withdraw")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "JWT 토큰이 비어있습니다.(J2001)"),
            @ApiResponse(code = 403 , message = "ACCESS-TOKEN이 맞지 않습니다.(J2002)", response = ApiErrorResponse.class),
    })
    public ApplicationResponse<MessageResponse> withdrawUser() throws BaseException {
        long userId = jwtService.getUserId();
        userService.withdrawUser(userId);
        projectService.deleteAllProjetsByUserId(userId);

        return ApplicationResponse.ok(MessageResponse.builder()
                .message("회원 탈퇴가 완료되었습니다.")
                .build());
    }



    /**
     * OAuth2
     */
    /**
     * 애플 로그인 API
     * [POST] api/v1/users/check-login-apple
     * @return ResponseEntity
    \     */
    @ApiOperation(value = "애플 로그인 - 첫번째 호출", notes = "userId 0 or jwt null : 추가정보 받는 apple-login api," +
            "\t\n requestBody : 필수, 토큰에 따른 에러처리, responseBody : 사용자 정보 조회 note 참조" +
            "\t\n 애플 로그인 response에 따른 에러처리")
    @PostMapping("/check-login-apple")
    @ApiResponses(value = {
            @ApiResponse(code = 405 , message = "애플 로그인에 실패했습니다.(A2001)"),
            @ApiResponse(code = 500 , message = "유효한 RSA값을 찾지 못했습니다.(A2002)", response = ApiErrorResponse.class),
    })
    public ApplicationResponse<LoginResponse> createAppleUser(@Valid @RequestBody AppleAccessRequest appleAccessRequest) {
        User user = oAuth2UserServiceApple.validateAppleAccessToken(appleAccessRequest.getIdentityToken());
        if (user.getStatus() == 0){
            return ApplicationResponse.ok(LoginResponse.builder()
                    .message("회원가입을 진행해주세요, apple-login api에서 추가정보를 입력해주세요")
                    .build());
        }
        if (user.getUserJob() == null) {
            return ApplicationResponse.ok(LoginResponse.builder()
                    .message("회원가입을 진행해주세요, apple-login api에서 추가정보를 입력해주세요")
                    .build());
        }
        return getLoginResponseResponseEntity(user);
    }

    /**
     * 애플 로그인 API
     * [POST] api/v1/users/apple-login
     * @param appleLoginRequest
     * @return ResponseEntity
    \     */
    @ApiOperation(value = "애플 로그인 - 추가정보 입력 후 호출", notes = "회원가입 후 로그인 - 추가 정보 받고 호출하는 api, 연차 0,1,2,3 으로 전달해주세요" +
            "\t\n 위와 같은 애플 api를 이용하고 있는데 위에서 검증을 하고 넘기기 때문에 그부분에 대한 에러코드는 생략했습니다." +
            "\t\n requestBody : 모두 필수값, 조건 : [세부직무 1~3개, 년차(0~3)](조건x : apiError), responseBody : 사용자 정보 조회 note 참조" +
            "\t\n 닉네임 패턴, 닉네임 존재 여부, 가입 여부, 중복된 세부 직무 여부")
    @PostMapping("/apple-login")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "유효하지 않은 닉네임 입니다.(U1007)"),
            @ApiResponse(code = 409, message = "중복된 닉네임 입니다.(U1001) \t\n 해당 유저는 이미 가입한 유저입니다.(U1004) \t\n 중복된 세부직무가 있습니다.(U1005)", response = ApiErrorResponse.class)
    })
    public ApplicationResponse<LoginResponse> loginKakaoUser(@Valid @RequestBody AppleLoginRequest appleLoginRequest) {
        User user = oAuth2UserServiceApple.validateAppleAccessToken(appleLoginRequest.getIdentityToken());
        if(user.getSocialId() == null){
            throw new AppleFailException();
        }

        if (!isRegexNickname(appleLoginRequest.getNickname())) {
            throw new UserNicknameValidException();
        }

        if (isRegexNicknameNum(appleLoginRequest.getNickname())) {
            throw new UserNicknameValidException();
        }

        if (user.getUserJob() != null && user.getStatus() ==1) {
            throw new UserDuplicateException();
        }

        this.checkDuplicateJob(appleLoginRequest.getUserDetailJob());

        if(!Objects.equals(user.getUserNickName(), appleLoginRequest.getNickname())){
            userService.userNicknameDuplicateCheck(appleLoginRequest.getNickname());

        }

        if (user.getStatus() ==0) {
            userJobService.updateDeleteUserJob(user);
        }

        // 회원가입 한 적 없는 경우 - 데이터 저장
        try {
            User finalUser = userService.insertUserApple(appleLoginRequest, user);
            userJobService.createUserJob(appleLoginRequest.getUserDetailJob(), finalUser);
            User u = colorService.getColorName(finalUser);
            return getLoginResponseResponseEntity(u);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // 로그인 정보 불러오기
    }

    /**
     * 애플 탈퇴 API
     * [POST] api/v1/users/apple-withdraw
     * @return ResponseEntity
    \     */
//    @ApiOperation(value = "애플 회원 탈퇴 - jwt 0 -", notes = "회원 탈퇴를 합니다. 애플 필수 사항, 탈퇴시 다시 로그인 - authorizationCode 전달" +
//            "userSocialProvider가 1인사람만 이걸로 !")
//    @PostMapping("/apple-withdraw")
//    public ApplicationResponse<MessageResponse> withdrawAppleUser(@Valid @RequestBody AppleWithdrawRequest appleWithdrawRequest) throws IOException {
////        long userId = jwtService.getUserId();
////        userService.withdrawUser(userId);
////        User user = userService.getUserByUserId(userId);
//
//        appleService.revoke(appleWithdrawRequest.getAuthorizationCode());
//
//        return ApplicationResponse.ok(MessageResponse.builder()
//                .message("회원 탈퇴가 완료되었습니다.")
//                .build());
//    }


    /**
     * 카카오 로그인 API
     * [POST] api/v1/users/check-login
     * @return ResponseEntity
    \     */
    @ApiOperation(value = "카카오 로그인 - 첫번째 호출", notes = "userId 0 or jwt null : 추가정보 받는 kakao-login api," +
            "\t\n requestBody : 필수, 토큰에 따른 에러처리, responseBody : 사용자 정보 조회 note 참조" +
            "\t\n 카카오 로그인 response에 따른 에러처리")
    @PostMapping("/check-login")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "카카오 로그인에 실패했습니다.(K2002)"),
            @ApiResponse(code = 401 , message = "카카오 인증에 실패했습니다.(K2001)"),
            @ApiResponse(code = 405 , message = "카카오의 지정된 요청 방식 이외의 프로토콜을 전달했습니다.(K2004)"),
            @ApiResponse(code = 500 , message = "카카오 API 응답을 읽는데 실패했습니다.(K2005) \t\n 카카오 API URL이 잘못되었습니다.(K2003)", response = ApiErrorResponse.class),
    })
    public ApplicationResponse<LoginResponse> checkKakaoUser(@Valid @RequestBody KakaoAccessRequest kakaoAccessRequest) {
        User user = oAuth2UserServiceKakao.validateKakaoAccessToken(kakaoAccessRequest.getAccessToken());
//        if(user.getSocialId() == null){
//            throw new KakaoFailException();
//        }
        if (user.getStatus() == 0){
            return ApplicationResponse.ok(LoginResponse.builder()
                    .message("회원가입을 진행해주세요, kakao-login api에서 추가정보를 입력해주세요")
                    .build());
        }
        // 회원가입 한 적 없는 경우 - 첫번째 호출
        if (user.getUserJob() == null) {
            return ApplicationResponse.ok(LoginResponse.builder()
                    .message("회원가입을 진행해주세요, kakao-login api에서 추가정보를 입력해주세요")
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
            "\t\n 위와 같은 카카오 api를 이용하고 있는데 위에서 검증을 하고 넘기기 때문에 그부분에 대한 에러코드는 생략했습니다." +
            "\t\n requestBody : 모두 필수값, 조건 : [세부직무 1~3개, 년차(0~3)](조건x : apiError), responseBody : 사용자 정보 조회 note 참조" +
            "\t\n 닉네임 패턴, 닉네임 존재 여부, 가입 여부, 중복된 세부 직무 여부")
    @PostMapping("/kakao-login")
    @ApiResponses(value = {
            @ApiResponse(code = 400 , message = "유효하지 않은 닉네임 입니다.(U1007)"),
            @ApiResponse(code = 409, message = "중복된 닉네임 입니다.(U1001) \t\n 해당 유저는 이미 가입한 유저입니다.(U1004) \t\n 중복된 세부직무가 있습니다.(U1005)", response = ApiErrorResponse.class)
    })
    public ApplicationResponse<LoginResponse> loginKakaoUser(@Valid @RequestBody KakaoLoginRequest kakaoLoginRequest) {
        User user = oAuth2UserServiceKakao.validateKakaoAccessToken(kakaoLoginRequest.getAccessToken());
        if(user.getSocialId() == null){
            throw new KakaoFailException();
        }

        if (!isRegexNickname(kakaoLoginRequest.getNickname())) {
            throw new UserNicknameValidException();
        }

        if (isRegexNicknameNum(kakaoLoginRequest.getNickname())) {
            throw new UserNicknameValidException();
        }

        if (user.getUserJob() != null && user.getStatus() ==1) {
            throw new UserDuplicateException();
        }

        this.checkDuplicateJob(kakaoLoginRequest.getUserDetailJob());

        if(!Objects.equals(user.getUserNickName(), kakaoLoginRequest.getNickname())){
            userService.userNicknameDuplicateCheck(kakaoLoginRequest.getNickname());

        }

        if (user.getStatus() ==0) {
            userJobService.updateDeleteUserJob(user);
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

    private void checkDuplicateJob(List<String> checkList){
        List<String> list = new ArrayList<>();
        for(String s: checkList){
            s = s.replaceAll(" ", "");
            list.add(s);
        }
        Set<String> set = new HashSet<>(list);
        if (set.size() != list.size()) {
            throw new UserJobDuplicateException();
        }
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
                .userJob(user.getUserJob())
                .jwt(jwt)
                .message("정보를 불러오는데 성공하였습니다.")
                .build());
    }

    private ApplicationResponse<InfoResponse> getInfoResponseResponseEntity(User user) {
        List<String> userJobList = userJobService.getUserJobName(user.getUserId());
        return ApplicationResponse.ok(InfoResponse.builder()
                .userId(user.getUserId())
                .userWork(user.getUserWork())
                .userIntro(user.getIntoContent())
                .userProfileImg(user.getUserProfileImg())
                .userProfileColor(user.getUserProfileColor())
                .userSocialProvider(user.getUserSocialProvider())
                .userDetailJobs(userJobList)
                .userNickname(user.getUserNickName())
                .userJob(user.getUserJob())
                .message("정보를 불러오는데 성공하였습니다.")
                .build());
    }





}