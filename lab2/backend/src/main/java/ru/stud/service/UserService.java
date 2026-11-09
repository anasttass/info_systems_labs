
package ru.stud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.stud.dto.RegisterRequest;
import ru.stud.dto.UserResponse;
import ru.stud.model.User;
import ru.stud.model.UserRoles;
import ru.stud.repository.UserRepository;
import ru.stud.util.UserMapper;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final String adminRegistrationCode;

    public UserService(
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            @Value("${app.admin-registration-code:}")
            String adminRegistrationCode
    ) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.adminRegistrationCode = adminRegistrationCode;
    }

    @Transactional
    public void createNewUser(RegisterRequest userForm) {

        if (userRepository.existsByLogin(userForm.login())) {
            throw new IllegalArgumentException(
                    "Пользователь с таким логином уже существует"
            );
        }

        UserRoles requestedRole = userForm.role();

        if (requestedRole == null) {
            requestedRole = UserRoles.GENERAL;
        }

        if (requestedRole == UserRoles.ADMIN) {
            if (adminRegistrationCode.isBlank()) {
                throw new IllegalStateException(
                        "Регистрация администраторов не настроена"
                );
            }

            if (userForm.adminCode() == null ||
                    !java.security.MessageDigest.isEqual(
                            adminRegistrationCode.getBytes(
                                    java.nio.charset.StandardCharsets.UTF_8
                            ),
                            userForm.adminCode().getBytes(
                                    java.nio.charset.StandardCharsets.UTF_8
                            )
                    )) {
                throw new AccessDeniedException(
                        "Неверный код администратора"
                );
            }
        } else if (requestedRole != UserRoles.GENERAL) {
            throw new IllegalArgumentException(
                    "Недопустимая роль пользователя"
            );
        }

        User user = userMapper.toEntity(userForm);

        user.setPassword(
                passwordEncoder.encode(userForm.password())
        );

        user.setRole(requestedRole);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse signIn(String login, String password) {

        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Неверный логин или пароль"
                        )
                );

        if (!passwordEncoder.matches(
                password,
                user.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "Неверный логин или пароль"
            );
        }

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public boolean loginExists(String login) {
        return userRepository.existsByLogin(login);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Пользователь не найден"
                        )
                );

        return userMapper.toResponse(user);
    }
}
