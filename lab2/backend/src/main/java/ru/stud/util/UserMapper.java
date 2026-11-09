package ru.stud.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.stud.dto.RegisterRequest;
import ru.stud.dto.UserResponse;
import ru.stud.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequest request);
}
