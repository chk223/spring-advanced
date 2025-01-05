package org.example.expert.config.logging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.config.util.UserRoleFinderByToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class Logger {
    private final ObjectMapper objectMapper;
    private final UserRoleFinderByToken finder;

    // 유저 권한 변경 API에 대한 포인트컷
    @Around("execution(* *..*Controller.changeUserRole(..))")
    public Object logUserRoleChange(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAdminAction(joinPoint);
    }

    // 댓글 삭제 API에 대한 포인트컷
    @Around("execution(* *..*Controller.deleteComment(..))")
    public Object logCommentDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAdminAction(joinPoint);
    }

    private Object logAdminAction(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // JWT 토큰에서 유저 정보 추출
        Map<Long,String> userInfo = finder.findUserRoleByToken(request);
        Long userId = userInfo.keySet().iterator().next();
        String userRoleStr = userInfo.get(userId);
        // 요청 정보 로깅
        String requestBody = "없음";
        if (joinPoint.getArgs().length > 0) {
            try {
                requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
            } catch (Exception e) {
                requestBody = "변환 실패: " + e.getMessage();
            }
        }

        log.info("관리자 기능 접근 유저 id : {}, 유저 등급: {}, 요청 시각: {}, 요청 URL: {}, 요청 본문: {}",
                userId, userRoleStr, LocalDateTime.now(), request.getRequestURI(), requestBody);

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 응답 정보 로깅
        String responseBody = "없음";
        if (result != null) {
            try {
                responseBody = objectMapper.writeValueAsString(result);
            } catch (Exception e) {
                responseBody = "변환 실패: " + e.getMessage();
            }
        }

        log.info("관리자 기능 처리 완료 - 유저 id : {}, 응답 본문: {}, 처리 시각: {}",
                userId, responseBody, LocalDateTime.now());

        return result;
    }
}