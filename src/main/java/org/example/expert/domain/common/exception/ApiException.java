package org.example.expert.domain.common.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiException extends BaseException {
    public ApiException(String message, HttpStatus status) {
        super(message,status);
    }
    public ApiException(String message, HttpStatus status, List<String> errorField) {
        super(message,status,errorField);
    }
}
