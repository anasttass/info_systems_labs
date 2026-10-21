
package ru.stud.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.stud.dto.HumanBeingResponse;
import ru.stud.dto.HumanBeingForm;
import ru.stud.model.HumanBeing;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HumanBeingMapper {

    // Entity -> DTO для таблицы
    @Mapping(target = "ownerId", source = "owner.id")
    HumanBeingResponse toResponse(HumanBeing entity);

    // DTO -> Entity
    // Владельца нельзя назначать из данных клиента
    @Mapping(target = "owner", ignore = true)
    HumanBeing toEntity(HumanBeingResponse dto);

    // Entity -> форма редактирования
    HumanBeingForm toForm(HumanBeing entity);

    // Форма -> Entity
    // Владельца устанавливает только backend
    @Mapping(target = "owner", ignore = true)
    HumanBeing toEntity(HumanBeingForm dto);

    List<HumanBeingResponse> toResponseList(
            List<HumanBeing> entities
    );
}
