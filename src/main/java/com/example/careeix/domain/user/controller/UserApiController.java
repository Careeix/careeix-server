package com.example.careeix.domain.user.controller;


import com.example.careeix.config.BaseResponse;
import com.example.careeix.domain.user.dto.KakaoLoginRequest;
import com.example.careeix.domain.user.dto.LoginResponse;
import com.example.careeix.domain.user.dto.MessageResponse;
import com.example.careeix.domain.user.dto.NicknameDuplicateRequest;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.exception.UserNicknameDuplicateException;
import com.example.careeix.domain.user.service.OAuth2UserServiceKakao;
import com.example.careeix.domain.user.service.UserService;
import com.example.careeix.utils.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;


@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Api(tags = "User API")
public class UserApiController {

    private final UserService userService;
    private final JwtService jwtService;
    private final OAuth2UserServiceKakao oAuth2UserServiceKakao;


    /**
     * 닉네임 중복확인
     * @param nicknameDuplicateRequest - valid
     * @return ResponseEntity<MessageResponse>
     */
    @ApiOperation(value = "닉네임 중복확인", notes = "닉네임 중복확인, valid 처리")
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다.", response = UserNicknameDuplicateException.class)
    })
    @PostMapping("/check-nickname")
    public ResponseEntity<MessageResponse> duplicateCheckUser(@Valid @RequestBody NicknameDuplicateRequest nicknameDuplicateRequest) {

        userService.userNicknameDuplicateCheck(nicknameDuplicateRequest.getUserNickname());

        return ResponseEntity.ok(MessageResponse.builder()
                .message("사용가능한 닉네임입니다.")
                .build());
    }

//    /**
//     * 사용자 정보 조회
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "사용자 정보 조회", notes = "사용자 정보를 조회합니다.")
//    @GetMapping("/profile")
//    public ResponseEntity<ProfileResponse> getUserProfile() {
//        String loginId = this.jwtService.getLoginId();
//        User user = userService.getUserProfile(loginId);
//        List<String> userThemaName = userThemaService.getUserThemaName(user.getUserId());
//        List<String> userRegionName = new ArrayList<>();
//        List<UserRegionResponse> regions = userRegionService.getUserRegion(user.getUserLoginId());
//
//        for (UserRegionResponse response : regions) {
//            userRegionName.add(response.getRegionName());
//        }
//
//        RatingGetResponse response = ratingService.getRating(user.getUserLoginId());
//
//        return ResponseEntity.ok(ProfileResponse.from(user, userThemaName, userRegionName, response.getRating()));
//    }
//
//    /**
//     * 사용자 정보 수정
//     * @param userProfileRequest, file, request
//     * @return ResponseEntity<String>
//     */
//    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보를 수정합니다.", produces = "multipart/form-data")
//    @ApiResponses(value = {
//            @ApiResponse(code = 409, message = "해당 닉네임은 이미 존재하는 닉네임입니다.(U0009) / 해당 이메일은 이미 존재하는 이메일입니다.(U0011)", response = UserNicknameDuplicateException.class),
//            @ApiResponse(code = 409, message = "해당 이메일은 이미 존재하는 이메일입니다.(U0011)", response = UserEmailDuplicateException.class)
//    })
//    @PostMapping("/update-profile")
//    public ResponseEntity<MessageResponse> updateUserProfile(@Valid @ModelAttribute UserProfileRequest userProfileRequest,
//                                                    @RequestParam(required = false) MultipartFile file) {
//
//        userThemaService.checkThemaName(userProfileRequest.getUserThemaName());
//        List<String> list = userProfileRequest.getUserRegionName();
//        String[] userRegionName = list.toArray(new String[list.size()]);
//        userRegionService.checkRegionName(userProfileRequest.getUserRegionName().toArray(userRegionName));
//
//        String loginId = jwtService.getLoginId();
//        User user = userService.updateUserProfile(loginId, userProfileRequest, file);
//        userThemaService.updateUserThema(userProfileRequest.getUserThemaName(), user);
//
//        UpdateUserRegionRequest request = new UpdateUserRegionRequest();
//        request.setUserRegions(userRegionName);
//        userRegionService.updateUserRegion(request, user.getUserLoginId());
//
//        return ResponseEntity.ok(MessageResponse.builder()
//                .message("사용자 정보가 수정되었습니다.")
//                .build());
//    }
//
//
//
//    /**
//     * 사용자 로그아웃
//     * @param request
//     * @return ResponseEntity<MessageResponse>
//     */
//    @ApiOperation(value = "사용자 로그아웃", notes = "사용자 로그아웃을 합니다.")
//    @GetMapping("/logout")
//    public ResponseEntity<MessageResponse> logoutUser(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.invalidate();
//
//        return ResponseEntity.ok(MessageResponse.builder()
//                .message("로그아웃에 성공했습니다.")
//                .build());
//    }
//
//    /**
//     * 회원 탈퇴
//     * @param withdrawUserRequest, request
//     * @return ResponseEntity<MessageResponse>
//     */
//    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴를 합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 400, message = "입력하신 비밀번호가 기존의 비밀번호와 일치하지 않습니다.(U0005)", response = NotEqualPasswordException.class)
//    })
//    @PostMapping("/withdraw")
//    public ResponseEntity<MessageResponse> withdrawUser(@Valid @RequestBody WithdrawUserRequest withdrawUserRequest) {
//        String loginId = jwtService.getLoginId();
//        userService.withdrawUser(withdrawUserRequest, loginId);
//
//        return ResponseEntity.ok(MessageResponse.builder()
//                .message("회원 탈퇴가 완료되었습니다.")
//                .build());
//    }



    /**
     * OAuth2
     */


    /**
     * 카카오 로그인 API
     * [POST] api/v1/users/kakao-login
     * @param kakaoLoginRequest
     * @return ResponseEntity
\     */
    @ApiOperation(value = "카카오 로그인", notes = "카카오 로그인을 합니다.")
    @PostMapping("/kakao-login")
    public ResponseEntity<LoginResponse> loginKakaoUser(@Valid @RequestBody KakaoLoginRequest kakaoLoginRequest) {
        User user = oAuth2UserServiceKakao.validateKakaoAccessToken(kakaoLoginRequest);
        return ResponseEntity.ok(LoginResponse.builder()
                .jwt(jwtService.createJwt(Math.toIntExact(user.getUserId())))
                .userNickname(user.getUserNickName())
                        .userEmail(user.getUserEmail())
                        .userJob(user.getUserJob())
                .message("로그인에 성공하였습니다.")
                .build());
    }



}