package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "사용자 정보 수정을 위한 요청 객체")
public class UserInfoRequest {

//    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "이메일 양식이 맞지 않습니다.")
//    @ApiModelProperty(notes = "이메일을 입력해 주세요.")
//    private String userEmail;

//    @NotBlank(message = "회원의 닉네임을 입력해주세요.")
//    @Pattern(regexp = "^[a-z0-9가-힣]{2,10}$", message = "닉네임은 2~10글자의 영소문자, 숫자, 한글만 가능합니다.")
//    @ApiModelProperty(notes = "닉네임을 입력해 주세요.")
//    private String userNickName;

    private String userJob;

    private int userWork;

    @Size(min = 1, max = 3, message = "상세 직업은 1개 이상 3개 이하로 작성해야 합니다.")
    private List<String> userDetailJob;

    private String intoContent;


}