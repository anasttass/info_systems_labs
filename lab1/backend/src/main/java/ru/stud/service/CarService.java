package ru.stud.service;

import ru.stud.model.Car;
import ru.stud.repository.CarRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService {

    private final CarRepository repository;

    public CarService(CarRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Car> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Car getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Автомобиль с ID " + id + " не найден"
                        )
                );
    }

    @Transactional
    public Car create(Car car) {
        return repository.save(car);
    }
}