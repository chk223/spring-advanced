package org.example.expert.domain.common.exception.util;

import org.example.expert.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ExceptionGenerator {
    public static <T extends BaseException> T generateExceptionOrThrow(ErrorMessage errorMessage, Class<T> exceptionClass) {
        try{
            return exceptionClass.getConstructor(String.class, HttpStatus.class)
                    .newInstance(errorMessage.getMessage(), errorMessage.getStatus());
        } catch (ReflectiveOperationException e){
            throw new IllegalArgumentException("예외 객체 생성 오류 발생!! 오류 예외 : " + exceptionClass.getName(), e);
        }
    }
}
