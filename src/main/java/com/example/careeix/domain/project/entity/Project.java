package com.example.careeix.domain.project.entity;

import com.example.careeix.config.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_career_id")
    private Long user_career_id;

    private String title;
    private String start_date;
    private String end_date;
    private int is_proceed;
    private String classification;
    private String introduction;

    /**
     * 연관관계 메서드
     */
    @OneToMany(mappedBy = "project")
    private List<ProjectDetail> projectDetails = new ArrayList<>();

    public void addProjectDetail(ProjectDetail projectDetail) {
        this.projectDetails.add(projectDetail);
        projectDetail.setProject(this);
    }


}
