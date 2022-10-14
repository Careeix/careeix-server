package com.example.careeix.domain.user.entity;

import com.example.careeix.config.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.example.careeix.domain.user.constant.UserConstants.EOAuth2UserServiceImpl.*;
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
    private String userEmail;
    private String userProfileImg;
    @ColumnDefault("'#1C1A81E5'")
    private String userProfileColor;
    private int userSocialProvider;
    private String intoContent;


    /**
     * 연관관계 메서드
     */
    @OneToMany(mappedBy = "user")
    private List<UserJob> userJobs = new ArrayList<>();


    public static User toEntityOfKakaoUser(HashMap<String, Object> userInfo) {
        return User.builder()
                .socialId(userInfo.get(eIdToken.getValue()).toString())
                .userEmail(userInfo.get(eEmailAttribute.getValue()).toString())
                .userProfileImg(userInfo.get(eKakaoProfileImageAttribute).toString())
                .userSocialProvider(eKakao.ordinal())
                .build();
    }



}
