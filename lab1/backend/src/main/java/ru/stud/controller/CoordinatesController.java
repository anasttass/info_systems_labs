package ru.stud.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.stud.dto.CoordinatesForm;
import ru.stud.service.CoordinatesService;

import java.util.List;


@RestController
@RequestMapping("/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;


    public CoordinatesController(
            CoordinatesService coordinatesService
    ) {
        this.coordinatesService = coordinatesService;
    }


    @GetMapping
    public List<CoordinatesForm> getAll() {

        return coordinatesService.getAll();
    }


    @GetMapping("/{id}")
    public CoordinatesForm getById(
            @PathVariable Long id
    ) {

        return coordinatesService.getById(id);
    }


    @PostMapping
    public CoordinatesForm create(
            @Valid @RequestBody CoordinatesForm form
    ) {

        return coordinatesService.create(form);
    }



    @PutMapping("/{id}")
    public CoordinatesForm update(
            @PathVariable Long id,
            @Valid @RequestBody CoordinatesForm form
    ) {

        return coordinatesService.update(id, form);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        coordinatesService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}