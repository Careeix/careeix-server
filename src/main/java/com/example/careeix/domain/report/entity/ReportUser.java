package com.example.careeix.domain.report.entity;

import com.example.careeix.config.BaseEntity;
import com.example.careeix.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_to")
    private User reportUserTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_from")
    private User reportUserFrom;

}
