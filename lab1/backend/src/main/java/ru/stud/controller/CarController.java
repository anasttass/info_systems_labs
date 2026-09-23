package ru.stud.controller;

import ru.stud.model.Car;
import ru.stud.service.CarService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // Список автомобилей

    @GetMapping
    public String getAll(Model model) {

        model.addAttribute(
                "cars",
                carService.getAll()
        );

        return "cars";
    }

    // Форма создания автомобиля

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute(
                "car",
                new Car()
        );

        return "car-create";
    }

    // Создание автомобиля

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("car") Car car,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "car-create";
        }

        carService.create(car);

        return "redirect:/cars";
    }
}