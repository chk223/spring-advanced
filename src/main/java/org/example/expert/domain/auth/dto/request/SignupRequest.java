package org.example.expert.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank @Email(message = "이메일")
    private String email;
    @NotBlank(message = "비밀번호")
    private String password;
    @NotBlank(message = "유저 역할")
    private String userRole;
}
