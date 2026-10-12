package ru.stud.util;

import org.mapstruct.Mapper;
import ru.stud.dto.CarForm;
import ru.stud.model.Car;

@Mapper(componentModel ="spring")
public interface CarMapper {
     CarForm toForm (Car car);
     Car toEntity (CarForm carForm);
}
