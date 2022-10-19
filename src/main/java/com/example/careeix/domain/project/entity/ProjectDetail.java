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
public class ProjectDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_detail_id;

    // FetchType.EAGER 전략 : 항상 목록을 가져오게 되어 있습니다
    // 불필요한 조회를 막으려면 FetchType.LAZY를 설정해야 합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String project_name;
    private String content;


    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectNote> projectNotes = new ArrayList<>();

    public void addProjectNote(ProjectNote projectNote) {
        this.projectNotes.add(projectNote);
        projectNote.setProjectDetail(this);
    }
}