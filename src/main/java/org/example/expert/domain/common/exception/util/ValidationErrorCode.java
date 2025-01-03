package org.example.expert.domain.common.exception.util;

import lombok.Getter;

@Getter
public enum ValidationErrorCode {
    VALIDATION_ERROR(1<<0, "잘못 된 입력입니다."),
    INVALID_ID(1<<1, "아이디"),
    INVALID_PASSWORD(1<<2, "비밀번호"),
    INVALID_EMAIL(1<<3, "이메일"),
    INVALID_COMMENT(1<<4,"댓글"),
    INVALID_ROLE(1 << 5, "역할"),
    GENERAL_ERROR(500, "예상치 못한 오류가 발생했습니다.");

    private final int code;
    private final String message;

    ValidationErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
