package ru.stud.dto;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
public class CarForm {

    private Long id;

    @NotNull
    private Boolean cool;

}