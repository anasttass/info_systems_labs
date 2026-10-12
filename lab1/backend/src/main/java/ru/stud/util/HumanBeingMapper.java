package ru.stud.util;

import org.mapstruct.Mapper;
import ru.stud.dto.HumanBeingResponse;
import ru.stud.model.HumanBeing;
import ru.stud.dto.HumanBeingForm;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HumanBeingMapper {

    HumanBeingResponse toResponse(HumanBeing entity);

    HumanBeing toEntity(HumanBeingResponse dto);

    HumanBeingForm toForm(HumanBeing entity);

    HumanBeing toEntity(HumanBeingForm dto);

    List<HumanBeingResponse> toResponseList(
            List<HumanBeing> entities
    );
}