package com.example.careeix.domain.project.dto;

import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.user.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "프로젝트 등록을 위한 요청 객체")
public class PostProjectRequest {

    //project
    private String title;
    private String start_date;
    private String end_date;
    private int is_proceed;
    private String classification;
    private String introduction;
    //projectDetail
    private List<PostProjectDetail> projectDetail;


    public Project toEntity(User userId) {
        return Project.builder()
                .user(userId)
                .title(title)
                .startDate(start_date)
                .endDate(end_date)
                .isProceed(is_proceed)
                .classification(classification)
                .introduction(introduction)
                .build();
    }

}
