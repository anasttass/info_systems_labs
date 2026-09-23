package ru.stud.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import java.time.LocalDate;

@Entity
@Table(name = "humanbeing")
public class HumanBeing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="coordinates_id",nullable = false)
    private Coordinates coordinates;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private boolean realHero;

    @Column
    private boolean hasToothpick;

    @OneToOne
    @JoinColumn(name = "car_id",nullable = false)
    private Car car;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column
    @DecimalMax(value = "357")
    private float impactSpeed;

    @Column(nullable = false)
    private String soundtrackName;

    @Column
    private int minutesOfWaiting;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType weaponType;

    //автоматически проставляется время создания сущности
    @PrePersist
    public void prePersist(){
        creationDate=LocalDate.now();
    }
}
