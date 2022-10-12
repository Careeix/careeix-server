package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "닉네임 중복 확인 객체")
public class NicknameDuplicateRequest {

    @NotBlank(message = "회원의 닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-z0-9가-힣]{2,10}$", message = "닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다.")
    @ApiModelProperty(notes = "닉네임을 입력해 주세요.")
    private String userNickname;


}