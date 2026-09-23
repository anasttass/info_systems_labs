package ru.stud.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import ru.stud.dto.HumanBeingForm;
import ru.stud.model.HumanBeing;
import ru.stud.model.Mood;
import ru.stud.model.WeaponType;

import ru.stud.service.CarService;
import ru.stud.service.CoordinatesService;
import ru.stud.service.HumanBeingService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/humanbeings")
public class HumanBeingController {
    private final CoordinatesService coordinatesService;

    private final CarService carService;

    private final HumanBeingService humanBeingService;

    public HumanBeingController(
            HumanBeingService humanBeingService,
            CoordinatesService coordinatesService,
            CarService carService
    ) {
        this.humanBeingService = humanBeingService;
        this.coordinatesService = coordinatesService;
        this.carService = carService;
    }

    // ============================
    // 1. ПОЛУЧЕНИЕ ВСЕХ ГЕРОЕВ
    // ============================

    @GetMapping
    public String getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {

        Page<HumanBeing> humanBeings =
                humanBeingService.getAll(
                        PageRequest.of(
                                page,
                                size,
                                Sort.by("id").ascending()
                        )
                );

        model.addAttribute("humanBeings", humanBeings);

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", humanBeings.getTotalPages());

        return "index";
    }

    // ============================
    // 2. ПОЛУЧЕНИЕ ГЕРОЯ ПО ID
    // ============================

    @GetMapping("/{id}")
    public String getById(
            @PathVariable Long id,
            Model model
    ) {

        HumanBeing humanBeing =
                humanBeingService.getById(id);

        model.addAttribute("humanBeing", humanBeing);

        return "details";
    }

    // ============================
    // 3. ФОРМА СОЗДАНИЯ ГЕРОЯ
    // ============================

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute(
                "humanBeing",
                new HumanBeingForm()
        );

        model.addAttribute(
                "coordinatesList",
                coordinatesService.getAll()
        );

        model.addAttribute(
                "cars",
                carService.getAll()
        );

        model.addAttribute(
                "moods",
                Mood.values()
        );

        model.addAttribute(
                "weaponTypes",
                WeaponType.values()
        );

        return "create";
    }

    // ============================
    // 4. СОЗДАНИЕ ГЕРОЯ
    // ============================

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("humanBeing")
            HumanBeingForm form,

            BindingResult bindingResult,

            Model model
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "coordinatesList",
                    coordinatesService.getAll()
            );

            model.addAttribute(
                    "cars",
                    carService.getAll()
            );

            model.addAttribute(
                    "moods",
                    Mood.values()
            );

            model.addAttribute(
                    "weaponTypes",
                    WeaponType.values()
            );

            return "create";
        }

        humanBeingService.create(form);

        return "redirect:/humanbeings";
    }

    // ============================
    // 5. ФОРМА РЕДАКТИРОВАНИЯ
    // ============================

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {

        HumanBeing hero =
                humanBeingService.getById(id);

        HumanBeingForm form =
                humanBeingService.toForm(hero);

        model.addAttribute(
                "humanBeing",
                form
        );

        model.addAttribute(
                "heroId",
                id
        );

        model.addAttribute(
                "coordinatesList",
                coordinatesService.getAll()
        );

        model.addAttribute(
                "cars",
                carService.getAll()
        );

        model.addAttribute(
                "moods",
                Mood.values()
        );

        model.addAttribute(
                "weaponTypes",
                WeaponType.values()
        );

        return "edit";
    }

    // ============================
    // 6. ОБНОВЛЕНИЕ ГЕРОЯ
    // ============================

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,

            @Valid @ModelAttribute("humanBeing")
            HumanBeingForm form,

            BindingResult bindingResult,

            Model model
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "heroId",
                    id
            );

            model.addAttribute(
                    "coordinatesList",
                    coordinatesService.getAll()
            );

            model.addAttribute(
                    "cars",
                    carService.getAll()
            );

            model.addAttribute(
                    "moods",
                    Mood.values()
            );

            model.addAttribute(
                    "weaponTypes",
                    WeaponType.values()
            );

            return "edit";
        }

        humanBeingService.update(id, form);

        return "redirect:/humanbeings";
    }
    // ============================
    // 7. УДАЛЕНИЕ ГЕРОЯ
    // ============================

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id
    ) {

        humanBeingService.delete(id);

        return "redirect:/humanbeings";
    }
}