package ru.stud.dto;

public record RegisterRequest(
        String login,
        String password,
        String role,
        String adminCode
) {
}