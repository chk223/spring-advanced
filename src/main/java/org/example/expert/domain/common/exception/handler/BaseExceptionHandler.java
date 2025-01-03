package org.example.expert.domain.common.exception.handler;



import org.example.expert.domain.common.exception.*;
import org.example.expert.domain.common.exception.util.ErrorMessageGenerator;
import org.example.expert.domain.common.exception.util.ErrorResponse;
import org.example.expert.domain.common.exception.util.ErrorResponseGenerator;
import org.example.expert.domain.common.exception.util.ValidationErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
@ControllerAdvice
public class BaseExceptionHandler {
    // 공통 예외 처리 로직
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleException(BaseException e) {
        List<String> errorField = e.getErrorField();

        if (errorField != null && !errorField.isEmpty()) {
            return setErrorResponseForValidation(e, errorField);  // 공통 필터링 및 에러 처리
        }

        return ErrorResponseGenerator.GenerateResponseEntity(e.getStatus().value(), e.getMessage(), e.getStatus());
    }

    // 필드에 맞는 에러 메시지 처리
    private ResponseEntity<ErrorResponse> setErrorResponseForValidation(BaseException e, List<String> errorField) {
        int combinedErrorCode = 0;
        // errorField에 따라 비트 연산으로 오류 코드 합산
        for (String field : errorField) {
            switch (field) {
                case "id":
                    combinedErrorCode |= ValidationErrorCode.INVALID_ID.getCode();
                    break;
                case "password":
                    combinedErrorCode |= ValidationErrorCode.INVALID_PASSWORD.getCode();
                    break;
                case "email":
                    combinedErrorCode |= ValidationErrorCode.INVALID_EMAIL.getCode();
                    break;
                case "comment":
                    combinedErrorCode |= ValidationErrorCode.INVALID_COMMENT.getCode();
                    break;
                case "userRole":
                    combinedErrorCode |= ValidationErrorCode.INVALID_ROLE.getCode();
                    break;
                default:
                    break;
            }
        }

        // 비트 연산된 오류 코드로 메시지 생성
        String errorMessage = ErrorMessageGenerator.generateErrorMessage(combinedErrorCode);
        return ErrorResponseGenerator.GenerateResponseEntity(e.getStatus().value(), errorMessage, combinedErrorCode, e.getStatus());
    }
}
