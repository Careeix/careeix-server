package com.example.careeix.domain.report.dto;

import com.example.careeix.domain.report.entity.ReportUser;
import com.example.careeix.domain.user.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "신고 DTO 객체")
public class ReportUserDTO {
    private User reportUserToid;
    private User reportUserFromid;


    public ReportUser toEntity() {
        return ReportUser.builder()
                .reportUserTo(reportUserToid)
                .reportUserFrom(reportUserFromid)
                .build();
    }
}
