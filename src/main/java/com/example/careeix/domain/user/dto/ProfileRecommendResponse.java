package com.example.careeix.domain.user.dto;

import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.entity.UserJob;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로필 추천 응답 객체")
public class ProfileRecommendResponse {
    private long userId;
    private String userJob;
    private List<String> userDetailJobs;
    private int userWork;
    private String userProfileColor;


    public static ProfileRecommendResponse from(User user, List<String> userDetailJobs) {
        return ProfileRecommendResponse.builder()
                .userId(user.getUserId())
                .userJob(user.getUserJob())
                .userDetailJobs(userDetailJobs)
                .userWork(user.getUserWork())
                .userProfileColor(user.getUserProfileColor())
                .build();
    }

}
