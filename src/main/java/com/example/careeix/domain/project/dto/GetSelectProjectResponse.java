package com.example.careeix.domain.project.dto;

import com.example.careeix.domain.project.entity.ProjectDetail;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로젝트 선택시 응답 객체")
public class GetSelectProjectResponse {

    private long project_id;
    private String title;
    private String start_date;
    private String end_date;
    private int is_proceed;
    private String classification;
    private List<String> projectDetails;
}
