package ru.stud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stud.model.Coordinates;

public interface CoordinatesRepository extends JpaRepository<Coordinates,Long> {
}
