package com.app.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
// 반환할 http status 값, 에러 코드, 에러메세지를 관리하는 Enum 클래스
    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "business exception test"),

    // 인증 && 인가
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization Header가 빈값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "인증 타입이 Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "해당 refresh token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "A-008", "관리자 Role이 아닙니다."),

    // 회원
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입 입니다.(memberType : KAKAO)"),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입된 회원 입니다."),
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-003", "해당 회원은 존재하지 않습니다."),




    //AI 서버 통신 문제
    NOT_SENT_HTTP(HttpStatus.NO_CONTENT,"H-001","HTTP 메세지를 생성하지 못하였습니다."),

    //파일
    NOT_DOWNLOAD_FILE(HttpStatus.NOT_FOUND,"F-001","해당 파일이 존재하지 않습니다."),
    // 문제
    NOT_GENERATE_PROBLEM(HttpStatus.NO_CONTENT,"P-001","문제를 생성하지 못하였습니다."),
    NOT_UPLOAD_PROBLEM(HttpStatus.INTERNAL_SERVER_ERROR,"P-002","문제를 업로드하지 못하였습니다."),

    //요약 정리
    NOT_GENERATE_SUMMARY(HttpStatus.NO_CONTENT,"S-001","요약정리를 생성하지 못하였습니다."),
    ;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

}
