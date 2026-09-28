package ru.stud.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;

public class CoordinatesForm {

    private Long id;

    @NotNull
    private Double x;

    @NotNull
    @DecimalMax(value = "781")
    private Float y;


    public CoordinatesForm() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Double getX() {
        return x;
    }


    public void setX(Double x) {
        this.x = x;
    }


    public Float getY() {
        return y;
    }


    public void setY(Float y) {
        this.y = y;
    }
}