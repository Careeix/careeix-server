package com.example.careeix.domain.user.dto;

import com.example.careeix.domain.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "카카오 로그인을 위한 요청 객체")
public class KakaoLoginRequest {

    @ApiModelProperty(notes = "카카오 엑세스 토큰 , 입력 값")
    private String accessToken;
    @NotBlank(message = "회원의 닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-z0-9가-힣]{2,10}$", message = "닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다.")
    private String nickname;

    @NotBlank(message = "회원의 직업을 입력해주세요.")
    private String job;

    private int userWork;

    @Size(min = 1, max = 3, message = "상세 직업은 1개 이상 3개 이하로 작성해야 합니다.")
    private List<String> userDetailJob;

    public User toEntity(Long userId, User kakaoUser) {
        User user = new User();
        user.setUserId(userId);
        user.setSocialId(kakaoUser.getSocialId());
        user.setUserSocialProvider(kakaoUser.getUserSocialProvider());
        user.setUserEmail(kakaoUser.getUserEmail());
        user.setUserNickName(this.nickname);
        user.setUserJob(this.job);
        user.setUserWork(this.userWork);
        return user;
    }
}
