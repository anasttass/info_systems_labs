package ru.stud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stud.dto.HumanBeingForm;
import ru.stud.dto.HumanBeingResponse;
import ru.stud.service.SpecialOperationsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/special")
public class SpecialOperationsController {

    private final SpecialOperationsService service;

    public SpecialOperationsController(SpecialOperationsService service) {
        this.service = service;
    }

    // 1. Группировка по soundtrackName
    @GetMapping("/group-by-soundtrack")
    public Map<String, Long> groupBySoundtrack() {
        return service.groupBySoundtrack();
    }

    // 2. Поиск по подстроке имени
    @GetMapping("/search-by-name")
    public List<HumanBeingResponse> searchByName(@RequestParam String substring) {
        return service.searchByName(substring);
    }

    // 3. Уникальные minutesOfWaiting
    @GetMapping("/unique-waiting")
    public List<Integer> uniqueWaiting() {
        return service.getUniqueWaitingMinutes();
    }

    // 4. Удалить без зубочисток
    @PostMapping("/delete-without-toothpick")
    public ResponseEntity<Void> deleteWithoutToothpick() {
        service.deleteWithoutToothpick();
        return ResponseEntity.noContent().build();
    }

    // 5. Всем SADNESS
    @PostMapping("/set-sadness")
    public ResponseEntity<Void> setSadness() {
        service.setSadnessForAll();
        return ResponseEntity.noContent().build();
    }
}