package org.example.expert.domain.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.ApiException;
import org.example.expert.domain.common.exception.util.ErrorResponse;
import org.example.expert.domain.common.exception.util.ErrorResponseGenerator;
import org.example.expert.domain.common.exception.util.ValidationErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(ApiException e) {
        log.info("ApiExceptionHandler: ApiException 처리 중");
        List<String> errorField = e.getErrorField();
        if (errorField != null && !errorField.isEmpty()) {
            log.info("검증 예외 처리 진행");
            return super.handleException(e);  // 검증 예외 처리
        }
        //api 오류 처리
        return ErrorResponseGenerator.GenerateResponseEntity(e.getStatus().value(), e.getMessage(), e.getStatus());
    }
    /**
     * 그 외의 예외 처리
     * @param e 잡은 예외 객체
     * @return 에외 던지기
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.info("GlobalExceptionHandler: GlobalException 처리 중 에러 ={}",e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(500, "예상치 못한 오류가 발생했습니다.");
        return ErrorResponseGenerator.GenerateResponseEntity(ValidationErrorCode.GENERAL_ERROR.getCode(), ValidationErrorCode.GENERAL_ERROR.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

