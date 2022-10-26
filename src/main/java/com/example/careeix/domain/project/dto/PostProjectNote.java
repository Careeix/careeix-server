package com.example.careeix.domain.project.dto;

import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로젝트 등록의 노트를 위한 객체")
public class PostProjectNote {

    private String content;

    public ProjectNote toEntity(ProjectDetail projectDetail) {
        return ProjectNote.builder()
                .projectDetail(projectDetail)
                .content(content)
                .build();
    }
}
