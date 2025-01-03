package org.example.expert.domain.common.exception.util;

public class ErrorMessageGenerator {

    public static String generateErrorMessage(int combinedErrorCode) {
        StringBuilder errorMessage = new StringBuilder();
        // 각 오류 코드에 대해 비트 연산으로 체크
        if ((combinedErrorCode & ValidationErrorCode.INVALID_ID.getCode()) != 0) {
            errorMessage.append("아이디 ");
        }
        if ((combinedErrorCode & ValidationErrorCode.INVALID_PASSWORD.getCode()) != 0) {
            if (!errorMessage.isEmpty()) errorMessage.append("또는 ");
            errorMessage.append("비밀번호 ");
        }
        if ((combinedErrorCode & ValidationErrorCode.INVALID_EMAIL.getCode()) != 0) {
            if (!errorMessage.isEmpty()) errorMessage.append("또는 ");
            errorMessage.append("이메일 ");
        }
        if ((combinedErrorCode & ValidationErrorCode.INVALID_COMMENT.getCode()) != 0) {
            if (!errorMessage.isEmpty()) errorMessage.append("또는 ");
            errorMessage.append("댓글 ");
        }
        if ((combinedErrorCode & ValidationErrorCode.INVALID_ROLE.getCode()) != 0) {
            if (!errorMessage.isEmpty()) errorMessage.append("또는 ");
            errorMessage.append("역할 ");
        }

        errorMessage.append("을 잘못 입력하셨습니다.");
        return errorMessage.toString();
    }
}
