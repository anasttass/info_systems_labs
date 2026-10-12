package ru.stud.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.stud.dto.CarForm;
import ru.stud.service.CarService;

import java.util.List;


@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;


    public CarController(CarService carService) {
        this.carService = carService;
    }


    @GetMapping
    public List<CarForm> getAll() {
        return carService.getAll();
    }


    @GetMapping("/{id}")
    public CarForm getById(@PathVariable Long id) {
        return carService.getById(id);
    }

    @PostMapping
    public CarForm create(@Valid @RequestBody CarForm form) {
        return carService.create(form);
    }

    @PutMapping("/{id}")
    public CarForm update(@PathVariable Long id, @Valid @RequestBody CarForm form) {
        return carService.update(id, form);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public List<CarForm> getAvailableCars() {
        return carService.getAvailableCars();
    }

    @GetMapping("/page")
    public Page<CarForm> getPage(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size) {

        return carService.getAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }
}