package com.example.careeix.domain.user.service;

import com.example.careeix.domain.user.dto.KakaoLoginRequest;
import com.example.careeix.domain.user.entity.User;

import javax.validation.Valid;

public interface UserService {


    void userNicknameDuplicateCheck(String nickName);

    void userEmailDuplicateCheck(String userEmail);

    User insertUser(@Valid KakaoLoginRequest kakaoLoginRequest, User user);


//    User updateUserProfile(String loginId, @Valid UserProfileRequest userProfileRequest, MultipartFile file);
//
//    void withdrawUser(@Valid WithdrawUserRequest withdrawUserRequest, String loginId);

    User getUserByUserId(Long userId);



}
