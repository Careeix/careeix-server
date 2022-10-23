package com.example.careeix.domain.project.entity;

import com.example.careeix.config.BaseEntity;
import com.example.careeix.domain.user.entity.User;
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
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String startDate;
    private String endDate;
    private int isProceed;
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
