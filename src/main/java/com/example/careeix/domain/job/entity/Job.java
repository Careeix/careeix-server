package com.example.careeix.domain.job.entity;

import com.example.careeix.config.BaseEntity;
import com.example.careeix.domain.user.entity.UserJob;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long jobId;
    private String jobName;


    @OneToMany(mappedBy = "job")
    private List<UserJob> userJobs = new ArrayList<>();
}
