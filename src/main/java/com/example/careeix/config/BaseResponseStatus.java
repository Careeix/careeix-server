package com.example.careeix.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, "1000", "OK.",HttpStatus.OK),
    SUCCESS_CREATE(true, "1001", "OK.",HttpStatus.CREATED),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, "2000", "입력값을 확인해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_JWT(false, "2001", "JWT를 입력해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_JWT(false, "2002", "유효하지 않은 JWT입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_JWT(false,"2003","권한이 없는 유저의 접근입니다.", HttpStatus.BAD_REQUEST),

    // users
    USERS_EMPTY_USER_ID(false, "2010", "유저 아이디 값을 확인해주세요.", HttpStatus.BAD_REQUEST),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, "2015", "이메일을 입력해주세요.", HttpStatus.BAD_REQUEST),
    POST_USERS_INVALID_EMAIL(false, "2016", "이메일 형식을 확인해주세요.", HttpStatus.BAD_REQUEST),
    POST_USERS_EXISTS_EMAIL(false,"2017","중복된 이메일입니다.", HttpStatus.BAD_REQUEST),

    // PROJECT
    EMPTY_TITLE(false, "2020", "프로젝트 제목을 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_START_DATE(false, "2021", "프로젝트 시작 날짜를 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_END_DATE(false, "2022", "프로젝트 종료 날짜를 입력해주세요.", HttpStatus.BAD_REQUEST),
    DISORDERED_DATE(false, "2023", "프로젝트 시작-종료 날짜를 순서에 맞게 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_CLASSIFICATION(false, "2024", "프로젝트 구분을 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_INTRODUCTION(false, "2025", "프로젝트 소개를 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_PDETAIL_TITLE(false, "2026", "프로젝트 내용 제목을 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_PDETAIL_CONTENT(false, "2027", "프로젝트 내용 본문을 입력해주세요.", HttpStatus.BAD_REQUEST),
    EMPTY_PNOTE_CONTENT(false, "2028", "프로젝트 메모 본문을 입력해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_ISPROCEED(false, "2029", "프로젝트 진행 여부 입력을 확인해주세요.(0 : 진행 종료, 1 : 진행 중)", HttpStatus.BAD_REQUEST),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, "3000", "값을 불러오는데 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // [POST] /users
    DUPLICATED_EMAIL(false, "3013", "중복된 이메일입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_LOGIN(false,"3014","없는 아이디거나 비밀번호가 틀렸습니다.", HttpStatus.INTERNAL_SERVER_ERROR),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, "4000", "데이터베이스 연결에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_ERROR(false, "4001", "서버와의 연결에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,"4014","유저네임 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR),

    PASSWORD_ENCRYPTION_ERROR(false, "4011", "비밀번호 암호화에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_DECRYPTION_ERROR(false, "4012", "비밀번호 복호화에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
//    private final int code;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    private BaseResponseStatus(boolean isSuccess, String code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
