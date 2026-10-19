package ru.stud.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.stud.config.PasswordConfig;
import ru.stud.dto.UserForm;
import ru.stud.model.User;
import ru.stud.repository.UserRepository;
import ru.stud.util.UserMapper;

public class UserService {
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public UserService(UserMapper userMapper,PasswordEncoder passwordEncoder,UserRepository userRepository){
        this.userMapper=userMapper;
        this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
    }



    @Transactional
    public void createNewUser(UserForm userForm){

        if (userRepository.existsByLogin(userForm.getLogin())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User user = userMapper.toEntity(userForm);
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    public UserForm signIn(String login, String password) {
        User user = (User) userRepository.findByLogin(login).orElseThrow(() -> new IllegalArgumentException("Неверный логин или пароль"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Неверный логин или пароль");
        }

        return userMapper.toForm(user);

    }

    @Transactional
    public boolean loginExists(String login) {
        return userRepository.existsByLogin(login);
    }

    @Transactional
    public UserForm getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        return userMapper.toForm(user);
    }
}
