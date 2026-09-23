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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public float getImpactSpeed() {
        return impactSpeed;
    }

    public void setImpactSpeed(float impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public int getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setMinutesOfWaiting(int minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public void setHasToothpick(boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public void setRealHero(boolean realHero) {
        this.realHero = realHero;
    }

    public boolean isRealHero() {
        return realHero;
    }

    public boolean isHasToothpick() {
        return hasToothpick;
    }
}
