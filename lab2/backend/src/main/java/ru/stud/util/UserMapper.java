package ru.stud.util;

import org.mapstruct.Mapper;
import ru.stud.dto.UserForm;
import ru.stud.model.User;

@Mapper(componentModel ="spring")
public interface UserMapper {
    UserForm toForm(User user);
    User toEntity(UserForm userForm);
}
