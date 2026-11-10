package ru.stud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter

@Entity
@Table(name = "lab2_car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private boolean cool;


    // Preserve the boxed getCool() API and its validation annotation.
    public @NotNull Boolean getCool() {
        return cool;
    }
}
