package com.example.careeix.domain.user.service;

import com.example.careeix.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserService {


    void userNicknameDuplicateCheck(String nickName);

    void userEmailDuplicateCheck(String userEmail);



//    User updateUserProfile(String loginId, @Valid UserProfileRequest userProfileRequest, MultipartFile file);
//
//    void withdrawUser(@Valid WithdrawUserRequest withdrawUserRequest, String loginId);

    User getUserByUserId(Long userId);



}
