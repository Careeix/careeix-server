package com.example.careeix.domain.user.entity;

import com.example.careeix.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailJob extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userDetailJobId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;
    private String userDetailJob;











}
