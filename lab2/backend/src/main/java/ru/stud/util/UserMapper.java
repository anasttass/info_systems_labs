
package ru.stud.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.stud.dto.UserForm;
import ru.stud.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "adminCode", ignore = true)
    UserForm toForm(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserForm userForm);
}
