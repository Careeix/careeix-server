package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "애플 로그인을 위한 요청 객체")
public class AppleAccessRequest {

    @ApiModelProperty(notes = "애플 엑세스 토큰 , 입력 값")
    private String identityToken;


}
