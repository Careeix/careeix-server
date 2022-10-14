package com.example.careeix.domain.user.entity;

import com.example.careeix.config.BaseEntity;
import com.example.careeix.domain.job.entity.Job;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJob extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userJobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_jobId")
    private Job job;

    public void setUser(User user) {
        this.user = user;
        user.getUserJobs().add(this);
    }

    public void setJob(Job job) {
        this.job = job;
        job.getUserJobs().add(this);
    }

    public static UserJob toEntityOfUserJob(User user, Job job) {
        return UserJob.builder()
                .user(user)
                .job(job)
                .build();
    }










}
