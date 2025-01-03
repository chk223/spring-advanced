package org.example.expert.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final List<String> errorField;
    public BaseException(String message, HttpStatus statusCode) {
        super(message);
        this.status = statusCode;
        this.errorField = Collections.emptyList(); // 빈 리스트로 초기화
    }

    public BaseException(String message, HttpStatus statusCode, List<String> errorField) {
        super(message);
        this.status = statusCode;
        this.errorField = errorField != null ? errorField : Collections.emptyList();
    }
}
