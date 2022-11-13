package com.example.careeix.domain.user.dto;



import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로필 이미지 수정 응답 객체")
public class ProfileImageModifyResponse {

    private String message;
    private String userProfileImg;


    public static LoginResponse from(String message) {
        return LoginResponse.builder()
                .message(message)
                .build();
    }

}
