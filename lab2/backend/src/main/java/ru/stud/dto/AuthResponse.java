package ru.stud.dto;

import lombok.Getter;
import lombok.Setter;
import ru.stud.model.UserRoles;

@Setter
@Getter

public class AuthResponse {
    private String token;
    private Long user_id;
    private String role;
    private String login;

    public AuthResponse(String token, Long user_id, String login, UserRoles role){
        this.token=token;
        this.user_id=user_id;
        this.login=login;
        this.role=role.name();
    }
}
