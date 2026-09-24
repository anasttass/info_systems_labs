package ru.stud.dto;

import jakarta.validation.constraints.*;

import ru.stud.model.Mood;
import ru.stud.model.WeaponType;

public class HumanBeingForm {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Необходимо выбрать координаты")
    private Long coordinatesId;

    @NotNull(message = "Необходимо указать, является ли герой настоящим")
    private Boolean realHero;

    @NotNull(message = "Необходимо указать наличие зубочистки")
    private Boolean hasToothpick;

    private Long carId;

    private Mood mood;

    @NotNull(message = "Укажите скорость")
    @DecimalMax(
            value = "357",
            message = "Скорость не может превышать 357"
    )
    private Float impactSpeed;

    @NotBlank(message = "Название саундтрека не может быть пустым")
    private String soundtrackName;

    @NotNull(message = "Укажите время ожидания")
    private Integer minutesOfWaiting;

    @NotNull(message = "Выберите оружие")
    private WeaponType weaponType;

    // GETTERS

    public String getName() {
        return name;
    }

    public Long getCoordinatesId() {
        return coordinatesId;
    }

    public Boolean getRealHero() {
        return realHero;
    }

    public Boolean getHasToothpick() {
        return hasToothpick;
    }

    public Long getCarId() {
        return carId;
    }

    public Mood getMood() {
        return mood;
    }

    public Float getImpactSpeed() {
        return impactSpeed;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public Integer getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    // SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinatesId(Long coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public void setRealHero(Boolean realHero) {
        this.realHero = realHero;
    }

    public void setHasToothpick(Boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public void setImpactSpeed(Float impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    public void setMinutesOfWaiting(Integer minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }
}