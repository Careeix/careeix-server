package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "카카오 로그인을 위한 요청 객체")
public class KakaoLoginRequest {

    @ApiModelProperty(notes = "카카오 엑세스 토큰 , 입력 값")
    private String accessToken;
    private String nickname;
    private String job;
    private int userWork;
    private List<String> userDetailJob;

}
