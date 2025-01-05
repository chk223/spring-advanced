package org.example.expert.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.config.util.UserRoleFinderByToken;
import org.example.expert.domain.common.exception.ApiException;
import org.example.expert.domain.common.exception.util.ErrorMessage;
import org.example.expert.domain.common.exception.util.ExceptionGenerator;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleInterceptor implements HandlerInterceptor {
    private final UserRoleFinderByToken finder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Long,String> userInfo = finder.findUserRoleByToken(request);
        Long userId = userInfo.keySet().iterator().next();
        String userRoleStr = userInfo.get(userId);
        UserRole userRole = UserRole.of(userRoleStr);
        validateUserRoleOrThrow(userRole);
        // ADMIN 권한일 경우 요청 통과
        return true;
    }

    private static void validateUserRoleOrThrow(UserRole userRole) {
        if (!UserRole.ADMIN.equals(userRole)) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.ADMIN_ROLE_REQUIRED, ApiException.class);
        }
    }

}
