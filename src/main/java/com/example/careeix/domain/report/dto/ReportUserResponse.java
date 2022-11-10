package com.example.careeix.domain.report.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "신고 결과 객체")
public class ReportUserResponse {

    private String result;
    private int status;
    private Long report_user_to_id;
    private String report_user_to_nickname;
}
