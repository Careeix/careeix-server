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
//FIXME
//    @NotBlank
//    private String userJob;
    private String userJob;

//FIXME
//    @Min(0)
//    @Max(3)
//    private int userWork;
    @Min(0)
    @Max(4)
    private int userWork;

//FIXME
//    @Size(min = 1, max = 3)
//    private List<String> userDetailJob;
    @Size(min = 0, max = 3)
    private List<String> userDetailJob;

    private String userIntro;


}