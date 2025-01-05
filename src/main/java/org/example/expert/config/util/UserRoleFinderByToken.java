package org.example.expert.config.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserRoleFinderByToken {
    private final JwtUtil jwtUtil;
    public Map<Long,String> findUserRoleByToken(HttpServletRequest request) {
        Map<Long,String> userInfo = new HashMap<>();
        String authHeader = request.getHeader("Authorization");
        String token = jwtUtil.substringToken(authHeader);
        Claims claims = jwtUtil.extractClaims(token);
        Long userId = Long.valueOf(claims.getSubject());
        String userRole = claims.get("userRole", String.class);
        userInfo.put(userId,userRole);
        return userInfo;
    }
}
