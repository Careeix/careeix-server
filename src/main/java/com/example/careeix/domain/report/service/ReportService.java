package com.example.careeix.domain.report.service;

import com.example.careeix.config.BaseException;
import com.example.careeix.domain.report.dto.ReportUserResponse;

public interface ReportService {

    ReportUserResponse reportUser(Long reportUserToId, Long reportUserFromId) throws BaseException;

}
