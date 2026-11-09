package ru.stud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import lombok.Setter;
import lombok.Getter;


@Getter
@Setter
@Entity
@Table(name="coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    @DecimalMax(value = "781")
    private Float y;

}
