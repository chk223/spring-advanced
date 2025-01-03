package org.example.expert.domain.common.exception.util;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int statusCode;
    private final String message;
    private int errorCode;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorResponse(int statusCode, String message, int errorCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.errorCode = errorCode;
    }
}
