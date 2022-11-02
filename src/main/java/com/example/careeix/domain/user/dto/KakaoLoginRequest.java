package com.example.careeix.domain.user.dto;

import com.example.careeix.domain.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "카카오 로그인을 위한 요청 객체")
public class KakaoLoginRequest {

    @ApiModelProperty(notes = "카카오 엑세스 토큰 , 입력 값")
    private String accessToken;
    @NotBlank
//    @Pattern(regexp = "^[a-z0-9가-힣]{2,10}$", message = "유효하지 않은 닉네임입니다.")
    private String nickname;

    @NotBlank
    private String job;

    @Min(0)
    @Max(3)
    private int userWork;

    @Size(min = 1, max = 3)
    private List<String> userDetailJob;

    public User toEntity(Long userId, User kakaoUser) {
        User user = new User();
        user.setUserId(userId);
        user.setSocialId(kakaoUser.getSocialId());
        user.setUserSocialProvider(kakaoUser.getUserSocialProvider());
        user.setUserNickName(this.nickname);
        user.setUserJob(this.job);
        user.setUserWork(this.userWork);
        return user;
    }
}
