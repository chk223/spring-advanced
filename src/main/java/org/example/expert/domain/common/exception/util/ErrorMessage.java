package org.example.expert.domain.common.exception.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    PASSWORD_IS_WRONG("비밀번호 입력이 잘못되었습니다.", HttpStatus.UNAUTHORIZED),
    TODO_NOT_FOUND("todo 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("유저 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MANAGER_NOT_FOUND("매니저 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("댓글 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BLANK_INPUT("입력 값은 비어있을 수 없습니다.", HttpStatus.BAD_REQUEST),
    VALID_ERROR("입력 값이 잘못되었습니다.",HttpStatus.BAD_REQUEST),
    DIFFERENT_USER("사용 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE),
    INVALID_TOKEN("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    UNCERTIFIED("사용자 인증에 실패했습니다.",HttpStatus.UNAUTHORIZED),
    EMAIL_NOT_FOUND("이메일 정보가 존재하지 않습니다.",HttpStatus.UNAUTHORIZED),
    EMAIL_IS_DUPLICATED("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST),
    AUTH_ANNOTATION_IS_MISSING("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.", HttpStatus.UNAUTHORIZED),
    SELF_MANAGER_IS_NOT_ALLOWED("일정 작성자는 본인을 담당자로 등록할 수 없습니다.",HttpStatus.BAD_REQUEST),
    INVALID_USER_INFO("해당 일정을 만든 유저가 유효하지 않습니다.",HttpStatus.BAD_REQUEST),
    USER_NOT_MATCH("해당 일정에 등록된 담당자가 아닙니다.",HttpStatus.BAD_REQUEST),
    PASSWORD_FORM_NOT_MATCH("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.",HttpStatus.BAD_REQUEST),
    PASSWORD_MUST_CHANGE("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.",HttpStatus.BAD_REQUEST),
    WEATHER_DATA_NOT_FOUND("날씨 데이터가 없습니다.",HttpStatus.FORBIDDEN),
    INVALID_TODAY_WEATHER("오늘에 해당하는 날씨 데이터를 찾을 수 없습니다.",HttpStatus.FORBIDDEN),
    INVALID_USER_ROLE("유효하지 않은 UerRole",HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus status;

    ErrorMessage(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
