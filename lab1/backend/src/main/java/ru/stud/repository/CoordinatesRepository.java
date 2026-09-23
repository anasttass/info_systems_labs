package ru.stud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stud.model.Coordinates;

@Repository
public interface CoordinatesRepository
        extends JpaRepository<Coordinates, Long> {

}