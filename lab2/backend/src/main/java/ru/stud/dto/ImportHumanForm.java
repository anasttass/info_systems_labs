package ru.stud.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ru.stud.model.Mood;
import ru.stud.model.WeaponType;

public record ImportHumanForm(
        @NotBlank String name,
        @NotNull @Valid CoordinatesForm coordinates,
        @NotNull Boolean realHero,
        @NotNull Boolean hasToothpick,
        @Valid CarForm car,
        Mood mood,
        @NotNull @DecimalMax("357") Double impactSpeed,
        @NotBlank String soundtrackName,
        @NotNull Integer minutesOfWaiting,
        @NotNull WeaponType weaponType
) {}
