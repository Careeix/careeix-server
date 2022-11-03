package com.example.careeix.domain.user.service;

import com.example.careeix.domain.user.dto.AppleLoginRequest;
import com.example.careeix.domain.user.dto.KakaoLoginRequest;
import com.example.careeix.domain.user.dto.UserInfoRequest;
import com.example.careeix.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

public interface UserService {


    void userNicknameDuplicateCheck(String nickName);


    User insertUser(@Valid KakaoLoginRequest kakaoLoginRequest, User user);

    User insertUserApple(@Valid AppleLoginRequest appleLoginRequest, User user);


    User getUserByUserId(Long userId);


    User updateUserProfile(long userId, String nickName, MultipartFile file);

    User withdrawUser(long userId);

    User updateUserInfo(long userId, UserInfoRequest userInfoRequest);
}
