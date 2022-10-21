package com.example.careeix.domain.project.dto;


import com.example.careeix.domain.project.entity.ProjectDetail;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로젝트 등록의 내용을 위한 객체")
public class PostProjectDetail {

    //ProjectDeatil
    private String project_detail_title;
    private String content;
    //ProjectDetail Note
    private List<PostProjectNote> projectNotes;

    public ProjectDetail toEntity() {
        return ProjectDetail.builder()
                .project_detail_title(project_detail_title)
                .content(content)
                .build();
    }
}
