package ru.stud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stud.model.Car;

public interface CarRepository extends JpaRepository<Car,Long> {
}
