package ru.stud.controller;

import ru.stud.service.SpecialOperationsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/special")
public class SpecialOperationsController {

    private final SpecialOperationsService service;

    public SpecialOperationsController(
            SpecialOperationsService service
    ) {
        this.service = service;
    }

    // Главная страница специальных операций

    @GetMapping
    public String showSpecialPage() {
        return "special";
    }

    // 1. Группировка по soundtrackName

    @GetMapping("/group-by-soundtrack")
    public String groupBySoundtrack(Model model) {

        model.addAttribute(
                "result",
                service.groupBySoundtrack()
        );

        return "special";
    }

    // 2. Поиск по подстроке имени

    @GetMapping("/search-by-name")
    public String searchByName(
            @RequestParam String substring,
            Model model
    ) {

        model.addAttribute(
                "result",
                service.searchByName(substring)
        );

        return "special";
    }

    // 3. Уникальные minutesOfWaiting

    @GetMapping("/unique-waiting")
    public String uniqueWaiting(Model model) {

        model.addAttribute(
                "result",
                service.getUniqueWaitingMinutes()
        );

        return "special";
    }

    // 4. Удалить героев без зубочисток

    @PostMapping("/delete-without-toothpick")
    public String deleteWithoutToothpick() {

        service.deleteWithoutToothpick();

        return "redirect:/humanbeings";
    }

    // 5. Установить максимально печальное настроение

    @PostMapping("/set-sadness")
    public String setSadness() {

        service.setSadnessForAll();

        return "redirect:/humanbeings";
    }
}