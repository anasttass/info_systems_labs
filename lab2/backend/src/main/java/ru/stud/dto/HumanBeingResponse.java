package ru.stud.dto;

import ru.stud.model.Car;
import ru.stud.model.Coordinates;
import ru.stud.model.Mood;
import ru.stud.model.WeaponType;

import java.time.LocalDate;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;


@Getter
@Setter
@NoArgsConstructor
public class HumanBeingResponse {

    private Long id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private boolean realHero;
    private boolean hasToothpick;
    private Car car;
    private Mood mood;
    private Double impactSpeed;
    private String soundtrackName;
    private Integer minutesOfWaiting;
    private WeaponType weaponType;
    private Long ownerId;
    private Long version;

}