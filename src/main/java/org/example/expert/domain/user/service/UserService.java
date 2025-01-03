package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.ApiException;
import org.example.expert.domain.common.exception.util.ErrorMessage;
import org.example.expert.domain.common.exception.util.ExceptionGenerator;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_FOUND, ApiException.class));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.PASSWORD_FORM_NOT_MATCH, ApiException.class);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_FOUND, ApiException.class));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.PASSWORD_MUST_CHANGE, ApiException.class);
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.PASSWORD_IS_WRONG, ApiException.class);
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }
}
