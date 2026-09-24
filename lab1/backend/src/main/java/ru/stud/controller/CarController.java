package ru.stud.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stud.model.Car;
import ru.stud.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // Получить все автомобили
    @GetMapping
    public List<Car> getAll() {
        return carService.getAll();
    }

    // Получить автомобиль по id
    @GetMapping("/{id}")
    public Car getById(@PathVariable Long id) {
        return carService.getById(id);
    }

    // Создать автомобиль
    @PostMapping
    public Car create(
            @Valid @RequestBody Car car
    ) {
        return carService.create(car);
    }

    // Изменить автомобиль
    @PutMapping("/{id}")
    public Car update(
            @PathVariable Long id,
            @Valid @RequestBody Car car
    ) {
        return carService.update(id, car);
    }

    // Удалить автомобиль
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}