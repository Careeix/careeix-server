package com.example.careeix.domain.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.annotation.Nullable;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "사용자 정보 수정을 위한 요청 객체")
public class UserInfoRequest {

    @NotBlank
    private String userJob;

    @Min(0)
    @Max(3)
    private int userWork;

    @Size(min = 1, max = 3)
    private List<String> userDetailJob;

    private String userIntro;


}