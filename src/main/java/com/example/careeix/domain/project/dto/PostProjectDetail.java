package com.example.careeix.domain.project.dto;


import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로젝트 등록 - 내용 객체")
public class PostProjectDetail {

    //ProjectDeatil
    private String project_detail_title;
    private String content;

    //FIXME
    // 🚨 projectNotes는 Mandatory가 아님..!
    //ProjectDetail Note
    @Nullable
    private List<PostProjectNote> projectNotes;

    public ProjectDetail toEntity(Project project) {
        return ProjectDetail.builder()
                .project(project)
                .projectDetailTitle(project_detail_title)
                .content(content)
                .build();
    }
}
