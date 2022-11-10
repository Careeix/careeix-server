package com.example.careeix.domain.user.entity;

import com.example.careeix.config.BaseEntity;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.report.entity.ReportUser;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.example.careeix.domain.user.constant.UserConstants.ESocialProvider.eKakao;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String socialId;
    private String userJob;
    private int userWork;
    private String userNickName;
    private String userProfileImg;
    private String userProfileColor;
    private int userSocialProvider;
    private String intoContent;


    /**
     * 연관관계 메서드
     */
    @OneToMany(mappedBy = "user")
    private List<UserJob> userJobs = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "reportUserTo")
    private List<ReportUser> reportUserToList = new ArrayList<>();

    @OneToMany(mappedBy = "reportUserFrom")
    private List<ReportUser> reportUserFromList = new ArrayList<>();

    public static User toEntityOfKakaoUser(HashMap<String, Object> userInfo) {
        return User.builder()
                .socialId((String) userInfo.get("id"))
                .userSocialProvider(eKakao.ordinal())
                .build();
    }



}
