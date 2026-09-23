package ru.stud.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "car")
public class Car {

    //я хз надо ли тут айди, в описании классов нет, но как без него
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean cool;

}
