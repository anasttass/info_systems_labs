package ru.stud.service;

import ru.stud.dto.HumanBeingForm;

import ru.stud.dto.HumanBeingResponse;
import ru.stud.model.HumanBeing;
import ru.stud.model.Mood;

import ru.stud.repository.HumanBeingRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;


@Service
public class SpecialOperationsService {

    private final HumanBeingRepository repository;


    public SpecialOperationsService(
            HumanBeingRepository repository
    ) {
        this.repository = repository;
    }



    // 1. СГРУППИРОВАТЬ ПО SOUNDTRACK

    @Transactional(readOnly = true)
    public Map<String, Long> groupBySoundtrack() {

        List<HumanBeing> heroes =
                repository.findAll();

        return heroes.stream()
                .collect(
                        Collectors.groupingBy(
                                HumanBeing::getSoundtrackName,
                                Collectors.counting()
                        )
                );
    }


    // 2. ПОИСК ПО ПОДСТРОКЕ ИМЕНИ

    @Transactional(readOnly = true)
    public List<HumanBeingResponse> searchByName(
            String substring
    ) {

        List<HumanBeing> heroes =
                repository.findAll();

        return heroes.stream()

                .filter(hero ->
                        hero.getName()
                                .contains(substring)
                )

                // Entity -> Response DTO
                .map(this::toResponse)

                .toList();
    }

    // 3. УНИКАЛЬНЫЕ MINUTES OF WAITING


    @Transactional(readOnly = true)
    public List<Integer> getUniqueWaitingMinutes() {

        List<HumanBeing> heroes =
                repository.findAll();

        return heroes.stream()

                .map(
                        HumanBeing::getMinutesOfWaiting
                )

                .distinct()

                .toList();
    }



    // 4. УДАЛИТЬ ГЕРОЕВ БЕЗ ЗУБОЧИСТОК


    @Transactional
    public void deleteWithoutToothpick() {

        List<HumanBeing> heroes =
                repository.findAll();


        List<HumanBeing> heroesToDelete =
                heroes.stream()

                        .filter(hero ->
                                !hero.isHasToothpick()
                        )

                        .toList();


        repository.deleteAll(
                heroesToDelete
        );


        repository.flush();
    }



    // 5. УСТАНОВИТЬ ВСЕМ SADNESS

    @Transactional
    public void setSadnessForAll() {

        List<HumanBeing> heroes =
                repository.findAll();


        for (HumanBeing hero : heroes) {

            hero.setMood(
                    Mood.SADNESS
            );
        }


        repository.saveAll(heroes);
    }



    // 6. ENTITY -> DTO


    private HumanBeingForm toForm(
            HumanBeing hero
    ) {

        HumanBeingForm form =
                new HumanBeingForm();


        form.setName(
                hero.getName()
        );


        form.setCoordinatesId(
                hero.getCoordinates().getId()
        );


        form.setRealHero(
                hero.isRealHero()
        );


        form.setHasToothpick(
                hero.isHasToothpick()
        );


        if (hero.getCar() != null) {

            form.setCarId(
                    hero.getCar().getId()
            );
        }


        form.setMood(
                hero.getMood()
        );


        form.setImpactSpeed(
                hero.getImpactSpeed()
        );


        form.setSoundtrackName(
                hero.getSoundtrackName()
        );


        form.setMinutesOfWaiting(
                hero.getMinutesOfWaiting()
        );


        form.setWeaponType(
                hero.getWeaponType()
        );


        return form;
    }
    private HumanBeingResponse toResponse(HumanBeing hero) {

        HumanBeingResponse response = new HumanBeingResponse();

        response.setId(hero.getId());
        response.setName(hero.getName());
        response.setCoordinates(hero.getCoordinates());
        response.setCreationDate(hero.getCreationDate());
        response.setRealHero(hero.isRealHero());
        response.setHasToothpick(hero.isHasToothpick());
        response.setCar(hero.getCar());
        response.setMood(hero.getMood());
        response.setImpactSpeed(hero.getImpactSpeed());
        response.setSoundtrackName(hero.getSoundtrackName());
        response.setMinutesOfWaiting(hero.getMinutesOfWaiting());
        response.setWeaponType(hero.getWeaponType());

        return response;
    }
}