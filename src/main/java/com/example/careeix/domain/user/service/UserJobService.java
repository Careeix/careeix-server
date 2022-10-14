package com.example.careeix.domain.user.service;


import com.example.careeix.domain.user.dto.KakaoLoginRequest;
import com.example.careeix.domain.user.dto.LoginResponse;
import com.example.careeix.domain.user.entity.User;

import java.util.List;

public interface UserJobService {
    List<String> getUserJobName(Long userId);

    void createUserJob(List<String> jobNameList, User user);
}
