package ru.stud.controller;

import ru.stud.model.Coordinates;
import ru.stud.service.CoordinatesService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    public CoordinatesController(
            CoordinatesService coordinatesService
    ) {
        this.coordinatesService = coordinatesService;
    }

    // Список координат

    @GetMapping
    public String getAll(Model model) {

        model.addAttribute(
                "coordinatesList",
                coordinatesService.getAll()
        );

        return "coordinates";
    }

    // Форма создания координат

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute(
                "coordinates",
                new Coordinates()
        );

        return "coordinates-create";
    }

    // Сохранение координат

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("coordinates")
            Coordinates coordinates,

            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "coordinates-create";
        }

        coordinatesService.create(coordinates);

        return "redirect:/coordinates";
    }
}