package ru.stud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "car")
public class Car {

    //я хз надо ли тут айди, в описании классов нет, но как без него
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private boolean cool;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCool(boolean cool) {
        this.cool = cool;
    }

    public boolean isCool() {
        return cool;
    }
}
