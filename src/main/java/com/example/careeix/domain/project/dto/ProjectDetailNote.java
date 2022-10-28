package com.example.careeix.domain.project.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로젝트 내용,노트 객체")
public class ProjectDetailNote {
    private PostProjectDetail projectDetail;
    private List<String> projectNote;
}
