package ru.stud.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;


@Getter
@Setter
@NoArgsConstructor
public class CoordinatesForm {

    private Long id;

    @NotNull
    private Double x;

    @NotNull
    @DecimalMax(value = "781")
    private Float y;


}