package ru.stud.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stud.dto.UserForm;
import ru.stud.model.User;
import ru.stud.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody UserForm form
    ) {
        userService.createNewUser(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserForm form) {
        UserForm user = userService.signIn(form.getLogin(), form.getPassword());

        //добавим создание JWT
        throw new UnsupportedOperationException(
                "Генерация JWT ещё не реализована"
        );
    }
}
