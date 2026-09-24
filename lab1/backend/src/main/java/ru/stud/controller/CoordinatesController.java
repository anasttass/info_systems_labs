package ru.stud.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stud.model.Coordinates;
import ru.stud.service.CoordinatesService;

import java.util.List;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    // Получить все координаты
    @GetMapping
    public List<Coordinates> getAll() {
        return coordinatesService.getAll();
    }

    // Получить координаты по id
    @GetMapping("/{id}")
    public Coordinates getById(@PathVariable Long id) {
        return coordinatesService.getById(id);
    }

    // Создать координаты
    @PostMapping
    public Coordinates create(
            @Valid @RequestBody Coordinates coordinates
    ) {
        return coordinatesService.create(coordinates);
    }

    // Изменить координаты
    @PutMapping("/{id}")
    public Coordinates update(
            @PathVariable Long id,
            @Valid @RequestBody Coordinates coordinates
    ) {
        return coordinatesService.update(id, coordinates);
    }

    // Удалить координаты
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coordinatesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}