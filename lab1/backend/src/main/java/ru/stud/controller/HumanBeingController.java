package ru.stud.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.stud.dto.HumanBeingForm;
import ru.stud.model.HumanBeing;
import ru.stud.model.Mood;
import ru.stud.model.WeaponType;
import ru.stud.service.HumanBeingService;

@RestController
@RequestMapping("/humanbeings")
public class HumanBeingController {

    private final HumanBeingService humanBeingService;

    public HumanBeingController(HumanBeingService humanBeingService) {
        this.humanBeingService = humanBeingService;
    }

    // Получить страницу героев
    @GetMapping
    public Page<HumanBeing> getAll(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size,
            @RequestParam(defaultValue = "id")
            String sortBy,
            @RequestParam(defaultValue = "asc")
            String direction,
            @RequestParam(required = false)
            String name,
            @RequestParam(required = false)
            Boolean realHero,
            @RequestParam(required = false)
            Boolean hasToothpick,
            @RequestParam(required = false)
            Mood mood,
            @RequestParam(required = false)
            WeaponType weaponType
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        return humanBeingService.getAll(PageRequest.of(page, size, sort), realHero, hasToothpick, mood, weaponType);
    }

    // Получить одного
    @GetMapping("/{id}")
    public HumanBeing getById(@PathVariable Long id) {
        return humanBeingService.getById(id);
    }

    // Создать
    @PostMapping
    public HumanBeing create(
            @Valid @RequestBody HumanBeingForm form
    ) {
        return humanBeingService.create(form);
    }

    // Изменить
    @PutMapping("/{id}")
    public HumanBeing update(
            @PathVariable Long id,
            @Valid @RequestBody HumanBeingForm form
    ) {
        return humanBeingService.update(id, form);
    }

    // Удалить
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        humanBeingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}