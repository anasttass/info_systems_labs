package ru.stud.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "lab2_loading_record")
public class LoadingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lab2LoadingRecordGenerator")
    @SequenceGenerator(name = "lab2LoadingRecordGenerator", sequenceName = "lab2_loading_record_seq", allocationSize = 50)
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
