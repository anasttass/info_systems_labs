package ru.stud.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stud.model.UserRoles;

@Getter
@Setter
@NoArgsConstructor

public class UserForm {
    private Long id;
    private String login;
    private String password;
    private UserRoles role;
    private String adminCode;
}

