package com.example.careeix.domain.project.entity;

import com.example.careeix.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectNoteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_detail_id")
    private ProjectDetail projectDetail;

    private String content;


}
