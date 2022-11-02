package com.example.careeix.domain.user.dto;

import com.example.careeix.domain.user.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "정보 응답 객체")
public class InfoResponse {
    private String message;
    private long userId;
    private String userJob;
    private List<String> userDetailJobs;
    private int userWork;
    private String userNickname;
    private String userProfileImg;
    private String userProfileColor;
    private String userIntro;
    private int userSocialProvider;


    public static InfoResponse from(User user) {
        return InfoResponse.builder()
                .build();
    }

}
