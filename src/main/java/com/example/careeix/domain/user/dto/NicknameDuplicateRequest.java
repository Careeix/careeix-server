package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
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

    @NotBlank
    private String userNickname;


}