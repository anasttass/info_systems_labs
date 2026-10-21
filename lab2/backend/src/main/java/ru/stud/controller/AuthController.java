package ru.stud.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.stud.dto.AuthResponse;
import ru.stud.dto.UserForm;
import ru.stud.service.UserService;
import ru.stud.tokens.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody UserForm form
    ) {
        userService.createNewUser(form);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserForm form
    ) {
        UserForm user = userService.signIn(
                form.getLogin(),
                form.getPassword()
        );

        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse(
                token,
                user.getId(),
                user.getLogin(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }
}