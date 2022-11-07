package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "애플 탈퇴를 위한 요청 객체")
public class AppleWithdrawRequest {

    private String authorizationCode;


}
