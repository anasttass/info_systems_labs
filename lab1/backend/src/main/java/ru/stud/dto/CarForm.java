package ru.stud.dto;

import jakarta.validation.constraints.NotNull;

public class CarForm {

    private Long id;

    @NotNull
    private Boolean cool;


    public CarForm() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getCool() {
        return cool;
    }


    public void setCool(Boolean cool) {
        this.cool = cool;
    }
}