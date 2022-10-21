package com.example.careeix.domain.user.service;

import com.example.careeix.domain.job.entity.Job;
import com.example.careeix.domain.job.repository.JobRepository;
import com.example.careeix.domain.user.dto.ProfileRecommendResponse;
import com.example.careeix.domain.user.entity.User;
import com.example.careeix.domain.user.entity.UserJob;
import com.example.careeix.domain.user.repository.UserJobRepository;
import com.example.careeix.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserJobServiceImpl implements UserJobService{
    private final EntityManager em;

    private final UserJobRepository userJobRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;


    @Override
    public List<String> getUserJobName(Long userId) {
        List<String> userJobNames = new ArrayList<String>();
        List<UserJob> userJobs = userJobRepository.findByUser_UserId(userId);

        if (!userJobs.isEmpty()) {

            for (UserJob userJob : userJobs) {
                Job job = jobRepository.findByJobId(userJob.getJob().getJobId()).get();
                userJobNames.add(job.getJobName());
            }
        }
        return userJobNames;
    }


    @Override
    @Transactional
    public void createUserJob(List<String> jobNameList, User user) {
        try {
            for (String jobName : jobNameList) {
                Job job = jobRepository.findByJobName(jobName)
                        .orElse(jobRepository.save(Job.builder()
                                .jobName(jobName)
                                .build()));

                UserJob userJob = new UserJob();
                userJob.setUser(user);
                userJobRepository.save(UserJob.toEntityOfUserJob(userJob.getUser(), job));
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     userJob list update
     */
    @Override
    public void updateUserJob(User user, List<String> jobNameList) {
        List<UserJob> userJobList = userJobRepository.findByUser_UserId(user.getUserId());
        userJobRepository.deleteAll(userJobList);
                for (String jobName : jobNameList) {

        Job job = jobRepository.findByJobName(jobName).orElse(jobRepository.save(Job.builder()
                            .jobName(jobName)
                                    .build()));
                    userJobRepository.save(UserJob.toEntityOfUserJob(user, job));

                }

//        for (String jobName : jobNameList) {
//            Job job = jobRepository.findByJobName(jobName)
//                    .orElse(jobRepository.save(Job.builder()
//                            .jobName(jobName)
//                                    .build()));
//





    }

    @Override
    public List<ProfileRecommendResponse> getProfile(User user) {
        List<String> userJobList = this.getUserJobName(user.getUserId());
        ProfileRecommendResponse.from(user, userJobList);

        return null;
    }


}
