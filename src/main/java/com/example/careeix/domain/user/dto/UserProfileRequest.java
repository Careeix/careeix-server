package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "사용자 프로필 수정을 위한 요청 객체")
public class UserProfileRequest {

    @NotBlank
//    @Pattern(regexp = "^[a-z0-9가-힣]{2,10}$", message = "닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다.")
//    @ApiModelProperty(notes = "닉네임을 입력해 주세요.")
    private String userNickName;


}