package ru.stud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import java.time.LocalDate;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "lab2_humanbeing")
public class HumanBeing {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String name;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Setter
    @Getter
    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private Boolean realHero;

    @Setter
    @Getter
    @Column
    private boolean hasToothpick;

    @Setter
    @Getter
    @OneToOne
    @JoinColumn(name = "car_id", nullable = true,unique = true)
    private Car car;

    @Getter
    @Setter
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Setter
    @Getter
    @Column
    @DecimalMax(value = "357")
    private double impactSpeed;

    @Setter
    @Getter
    @Column(nullable = false)
    private String soundtrackName;

    @Setter
    @Getter
    @Column
    private int minutesOfWaiting;

    @Setter
    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType weaponType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    @Getter
    @Setter
    private User owner;

    @Version
    @Getter
    @Setter
    private Long version;


    // Автоматически проставляется дата создания сущности
    @PrePersist
    public void prePersist() {
        creationDate = LocalDate.now();
    }


    // Preserve primitive boolean accessors for the nullable Boolean field.
    public boolean isRealHero() {
        return realHero;
    }


    public void setRealHero(boolean realHero) {
        this.realHero = realHero;
    }

}