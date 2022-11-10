package com.example.careeix.domain.report.service;

import com.example.careeix.config.BaseException;
import com.example.careeix.domain.report.dto.ReportUserDTO;
import com.example.careeix.domain.report.dto.ReportUserResponse;
import com.example.careeix.domain.report.entity.ReportUser;
import com.example.careeix.domain.report.repository.ReportUserRepository;
import com.example.careeix.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.careeix.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.careeix.config.BaseResponseStatus.REPORT_ID_EQUAL;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final ReportUserRepository reportUserRepository;
    private final UserRepository userRepository;


    @Override
    public ReportUserResponse reportUser(Long reportUserToId, Long reportUserFromId) throws BaseException{
        try{
            //TODO
            // 이미 DB에 해당하는 column 존재하는지 확인
            // - 존재 O : 신고 추가, 신고가 접수 되었습니다 return
            // - 존재 X : 신고 취소, 신고가 취소 되었습니다 return
            // 자기 자신일 경우 exception

            Optional<ReportUser> isExist = reportUserRepository.findByReportUserToAndReportUserFrom(
                    userRepository.findByUserId(reportUserToId).get(),
                    userRepository.findByUserId(reportUserFromId).get());


            if (!isExist.isEmpty()) {
                reportUserRepository.delete(isExist.get());
                return new ReportUserResponse("사용자 신고가 취소 되었습니다.",0,isExist.get().getReportUserTo().getUserId(),isExist.get().getReportUserTo().getUserNickName());
            } else {
                ReportUserDTO reportUserDTO = new ReportUserDTO(userRepository.findByUserId(reportUserToId).get()
                                                                , userRepository.findByUserId(reportUserFromId).get());
                ReportUser newReportUser = reportUserRepository.save(reportUserDTO.toEntity());
                return new ReportUserResponse("사용자 신고가 접수 되었습니다.",1,newReportUser.getReportUserTo().getUserId(),newReportUser.getReportUserTo().getUserNickName());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
