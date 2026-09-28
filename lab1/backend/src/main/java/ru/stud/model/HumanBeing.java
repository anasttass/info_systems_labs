package ru.stud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "humanbeing")
public class HumanBeing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private Boolean realHero;

    @Column
    private boolean hasToothpick;

    @OneToOne
    @JoinColumn(name = "car_id", nullable = true,unique = true)
    private Car car;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column
    @DecimalMax(value = "357")
    private double impactSpeed;

    @Column(nullable = false)
    private String soundtrackName;

    @Column
    private int minutesOfWaiting;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType weaponType;

    // Автоматически проставляется дата создания сущности
    @PrePersist
    public void prePersist() {
        creationDate = LocalDate.now();
    }

    // ==================== GETTERS ====================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public boolean isRealHero() {
        return realHero;
    }

    public boolean isHasToothpick() {
        return hasToothpick;
    }

    public Car getCar() {
        return car;
    }

    public Mood getMood() {
        return mood;
    }

    public double getImpactSpeed() {
        return impactSpeed;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public int getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    // ==================== SETTERS ====================

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setRealHero(boolean realHero) {
        this.realHero = realHero;
    }

    public void setHasToothpick(boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public void setImpactSpeed(double impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    public void setMinutesOfWaiting(int minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }
}