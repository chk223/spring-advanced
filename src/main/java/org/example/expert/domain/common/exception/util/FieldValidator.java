package org.example.expert.domain.common.exception.util;

import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FieldValidator {
    public static void validateFieldErrorAndThrow(BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorFields = result.getFieldErrors().stream()
                    .map(FieldError::getField).toList();
            for(String f: errorFields) {
                log.info("에러 필드: {}", f);
            }
            throw new ApiException(ErrorMessage.VALID_ERROR.getMessage(), HttpStatus.BAD_REQUEST,errorFields);
        }
    }
}
