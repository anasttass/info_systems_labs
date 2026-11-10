package ru.stud.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity(name = "users")
@Table(name = "lab2_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lab2UsersGenerator")
    @SequenceGenerator(name = "lab2UsersGenerator", sequenceName = "lab2_users_seq", allocationSize = 50)
    private Long id;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoles role;
}
