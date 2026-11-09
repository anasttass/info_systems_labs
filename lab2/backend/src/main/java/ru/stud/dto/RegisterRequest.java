package ru.stud.dto;

import jakarta.validation.constraints.NotBlank;
import ru.stud.model.UserRoles;
import ru.stud.validation.ValidBcryptPassword;

public record RegisterRequest(
        @NotBlank String login,
        @NotBlank @ValidBcryptPassword String password,
        UserRoles role,
        String adminCode
) {
    @Override
    public String toString() {
        return "RegisterRequest[credentials redacted]";
    }
}
