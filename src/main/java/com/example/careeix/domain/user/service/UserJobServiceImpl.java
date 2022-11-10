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
import java.util.Objects;
import java.util.stream.Collectors;


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
//                Job job = jobRepository.findByJobName(jobName)
//                        .orElse(jobRepository.save(Job.builder()
//                                .jobName(jobName)
//                                .build()));
                Job job = jobRepository.save(Job.builder()
                                .jobName(jobName)
                                .build());

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
        for(int i=0; i<userJobList.size(); i++){
            jobRepository.delete(jobRepository.getOne(userJobList.get(i).getJob().getJobId()));
        }

        userJobRepository.deleteAll(userJobList);
//        jobRepository.deleteAll();
                for (String jobName : jobNameList) {
                    Job job = jobRepository.save(Job.builder()
                            .jobName(jobName)
                                    .build());

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
    public void updateDeleteUserJob(User user) {
        List<UserJob> userJobList = userJobRepository.findByUser_UserId(user.getUserId());
        for(int i=0; i<userJobList.size(); i++){
            jobRepository.delete(jobRepository.getOne(userJobList.get(i).getJob().getJobId()));
        }

        userJobRepository.deleteAll(userJobList);

    }

    @Override
    public List<ProfileRecommendResponse> getProfile(User user) {
        List<String> userJobList = this.getUserJobName(user.getUserId());
//        userJobList.add(0, user.getUserJob());
        List<String> distinctUserJobList = userJobList.stream().distinct().collect(Collectors.toList());
        List<ProfileRecommendResponse> profileRecommendResponses = new ArrayList<>();
        List<User> distinctUser = new ArrayList<>();
        distinctUser.add(user);

        // 사용자의 세무직무 회문
        for(String job: distinctUserJobList){
            if(profileRecommendResponses.size()>6){
                break;
            }
            // 사용자 세부직무와 같은 유저를 찾기 - string이면 string과 같은 Job여러개가 담김 각각은 당연히 유저가다름
            List<Job> findJob = jobRepository.findByJobName(job);
//            List<User> findUser = userRepository.findByUserJob(job);
            // 사용자 세부 직무랑 같은 다른 유저가 없으면 다른 세부직무로 돌리기
            if(findJob.isEmpty()){
                break;
            }
            // 각 job에 대한 유저찾기 (string 유저 2, ios 유저 2 이렇게 두번 담기는 경우는 제외해줘야)
            for(Job j : findJob){
                if(profileRecommendResponses.size()>6){
                    break;
                }
                User u = userJobRepository.findByJob_JobId(j.getJobId()).get(0).getUser();
                // 중복 된 경우엔 추가하지 않음
                if(distinctUser.contains(u))
                    break;
                List<String> findUserJobList = this.getUserJobName(u.getUserId());
                profileRecommendResponses.add(ProfileRecommendResponse.from(u, findUserJobList));
                distinctUser.add(u);
                }
            }


        if (profileRecommendResponses.isEmpty()){
            return null;
        }

        return profileRecommendResponses;
    }


}
