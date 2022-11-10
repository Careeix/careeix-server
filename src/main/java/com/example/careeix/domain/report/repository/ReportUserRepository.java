package com.example.careeix.domain.report.repository;

import com.example.careeix.domain.report.entity.ReportUser;
import com.example.careeix.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportUserRepository extends JpaRepository<ReportUser, Long> {


    Optional<ReportUser> findByReportUserToAndReportUserFrom(User toId, User fromId);

}
