package ru.stud.dto;

import jakarta.validation.constraints.*;

import ru.stud.model.Mood;
import ru.stud.model.WeaponType;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter

public class HumanBeingForm {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Необходимо выбрать координаты")
    private Long coordinatesId;

    @NotNull(message = "Необходимо указать, является ли герой настоящим")
    private Boolean realHero;

    @NotNull(message = "Необходимо указать наличие зубочистки")
    private Boolean hasToothpick;

    private Long carId;

    private Mood mood;

    @NotNull(message = "Укажите скорость")
    @DecimalMax(
            value = "357",
            message = "Скорость не может превышать 357"
    )
    private Double impactSpeed;

    @NotBlank(message = "Название саундтрека не может быть пустым")
    private String soundtrackName;

    @NotNull(message = "Укажите время ожидания")
    private Integer minutesOfWaiting;

    @NotNull(message = "Выберите оружие")
    private WeaponType weaponType;


}