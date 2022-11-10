package com.example.careeix.domain.user.dto;

import com.example.careeix.domain.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "애플 로그인을 위한 요청 객체")
public class AppleLoginRequest {

    @ApiModelProperty(notes = "애플 엑세스 토큰 , 입력 값")
    private String identityToken;
    @NotBlank
    private String nickname;

    @NotBlank
    private String job;

    @Min(0)
    @Max(3)
    private int userWork;

    @Size(min = 1, max = 3)
    private List<String> userDetailJob;

    public User toEntity(Long userId, User appleUser) {
        User user = new User();
        user.setUserId(userId);
        user.setSocialId(appleUser.getSocialId());
        user.setUserSocialProvider(appleUser.getUserSocialProvider());
        user.setUserNickName(this.nickname);
        user.setUserJob(this.job);
        user.setUserWork(this.userWork);
        return user;
    }
}
