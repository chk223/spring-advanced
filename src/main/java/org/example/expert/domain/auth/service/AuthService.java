package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.util.JwtUtil;
import org.example.expert.config.util.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.common.exception.ApiException;
import org.example.expert.domain.common.exception.util.ErrorMessage;
import org.example.expert.domain.common.exception.util.ExceptionGenerator;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        validateEmailOrThrow(signupRequest);
        String bearerToken = saveUserAndCreateToken(signupRequest);
        return new SignupResponse(bearerToken);
    }

    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
                () -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_FOUND, ApiException.class));
        validatePasswordOrThrow(signinRequest, user);
        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
        return new SigninResponse(bearerToken);
    }

    private String saveUserAndCreateToken(SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );
        User savedUser = userRepository.save(newUser);
        return jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);
    }

    private void validateEmailOrThrow(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.EMAIL_IS_DUPLICATED, ApiException.class);
        }
    }

    private void validatePasswordOrThrow(SigninRequest signinRequest, User user) {
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.PASSWORD_IS_WRONG, ApiException.class);
        }
    }
}
