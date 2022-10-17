package com.example.careeix.domain.user.service;


import com.example.careeix.domain.user.entity.User;

import java.util.List;

public interface UserJobService {
    List<String> getUserJobName(Long userId);

    void createUserJob(List<String> jobNameList, User user);

    void updateUserJob(User user, List<String> jobNameList);
}
