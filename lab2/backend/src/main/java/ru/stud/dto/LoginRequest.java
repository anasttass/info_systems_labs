package ru.stud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotBlank String login,
        @NotEmpty String password
) {
    @Override
    public String toString() {
        return "LoginRequest[credentials redacted]";
    }
}
