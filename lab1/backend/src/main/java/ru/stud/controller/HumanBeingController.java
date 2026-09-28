package ru.stud.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.stud.dto.HumanBeingForm;
import ru.stud.dto.HumanBeingResponse;
import ru.stud.model.Mood;
import ru.stud.model.WeaponType;
import ru.stud.service.HumanBeingService;


@RestController
@RequestMapping("/humanbeings")
public class HumanBeingController {

    private final HumanBeingService humanBeingService;


    public HumanBeingController(
            HumanBeingService humanBeingService
    ) {
        this.humanBeingService = humanBeingService;
    }


    // 1. ПОЛУЧИТЬ СТРАНИЦУ ГЕРОЕВ

    @GetMapping
    public Page<HumanBeingResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean realHero,
            @RequestParam(required = false) Boolean hasToothpick,
            @RequestParam(required = false) Mood mood,
            @RequestParam(required = false) WeaponType weaponType
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        return humanBeingService.getAll(
                pageable,
                realHero,
                hasToothpick,
                mood,
                weaponType
        );
    }


    // 2. ПОЛУЧИТЬ ОДНОГО ГЕРОЯ


    @GetMapping("/{id}")
    public HumanBeingResponse getById(@PathVariable Long id) {
        return humanBeingService.getById(id);
    }


    // 3. СОЗДАТЬ ГЕРОЯ


    @PostMapping
    public HumanBeingForm create(
            @Valid @RequestBody HumanBeingForm form
    ) {

        return humanBeingService.create(form);
    }

    // 4. ИЗМЕНИТЬ ГЕРОЯ


    @PutMapping("/{id}")
    public HumanBeingForm update(

            @PathVariable Long id,

            @Valid @RequestBody HumanBeingForm form

    ) {

        return humanBeingService.update(id, form);
    }



    // 5. УДАЛИТЬ ГЕРОЯ


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        humanBeingService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
