package com.example.careeix.domain.project.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로젝트 조회 응답 객체")
public class GetProjectResponse {
    private long project_id;
    private String title;
    private String start_date;
    private String end_date;
    private int is_proceed;
    private String classification;
    private String introduction;
}
