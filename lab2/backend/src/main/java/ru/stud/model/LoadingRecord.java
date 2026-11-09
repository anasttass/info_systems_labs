package ru.stud.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class LoadingRecord {

    @Id
    @GeneratedValue
    private Long id;


    @JoinColumn(name = "user_id")
    @ManyToOne
    private User creator;

    @Column
    @Enumerated(EnumType.STRING)
    private RecordStatus status;

    @Column(name = "number_of_entities")
    private Integer numberOfEntities;
}
