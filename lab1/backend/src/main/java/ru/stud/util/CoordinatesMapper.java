package ru.stud.util;

import org.mapstruct.Mapper;
import ru.stud.dto.CoordinatesForm;
import ru.stud.model.Coordinates;

@Mapper(componentModel = "spring")
public interface CoordinatesMapper {
    CoordinatesForm toForm(Coordinates coordinates);
    Coordinates toEntity(CoordinatesForm coordinatesForm);
}
