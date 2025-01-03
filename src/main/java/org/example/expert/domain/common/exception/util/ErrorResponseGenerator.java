package org.example.expert.domain.common.exception.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Slf4j
public class ErrorResponseGenerator {
    public static ResponseEntity<ErrorResponse> GenerateResponseEntity(int status, String message, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
    public static ResponseEntity<ErrorResponse> GenerateResponseEntity(int status, String message, int code, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(status, message, code);
        log.info("에러코드 : {}", code);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}