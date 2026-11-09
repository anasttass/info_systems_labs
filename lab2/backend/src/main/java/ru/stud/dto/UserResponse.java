package ru.stud.dto;

import ru.stud.model.UserRoles;

public record UserResponse(Long id, String login, UserRoles role) {
}
