package ru.stud.service;

import ru.stud.dto.HumanBeingForm;

import ru.stud.model.Car;
import ru.stud.model.Coordinates;
import ru.stud.model.HumanBeing;
import ru.stud.model.Mood;
import ru.stud.model.WeaponType;

import ru.stud.repository.HumanBeingRepository;
import ru.stud.repository.CoordinatesRepository;
import ru.stud.repository.CarRepository;

import ru.stud.exception.ResourceNotFoundException;
import ru.stud.exception.ObjectDeletionException;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class HumanBeingService {

    private final HumanBeingRepository humanBeingRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final CarRepository carRepository;


    public HumanBeingService(
            HumanBeingRepository humanBeingRepository,
            CoordinatesRepository coordinatesRepository,
            CarRepository carRepository
    ) {
        this.humanBeingRepository = humanBeingRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.carRepository = carRepository;
    }



    // 1. ПОЛУЧЕНИЕ ВСЕХ ГЕРОЕВ


    @Transactional(readOnly = true)
    public List<HumanBeingForm> getAll() {

        return humanBeingRepository.findAll()
                .stream()
                .map(this::toForm)
                .toList();
    }



    // 2. ПОЛУЧЕНИЕ СТРАНИЦЫ С ФИЛЬТРАЦИЕЙ


    @Transactional(readOnly = true)
    public Page<HumanBeingForm> getAll(
            Pageable pageable,
            Boolean realHero,
            Boolean hasToothpick,
            Mood mood,
            WeaponType weaponType
    ) {

        return humanBeingRepository
                .findAllWithFilters(
                        realHero,
                        hasToothpick,
                        mood,
                        weaponType,
                        pageable
                )
                .map(this::toForm);
    }



    // 3. ПОЛУЧЕНИЕ ГЕРОЯ ПО ID


    @Transactional(readOnly = true)
    public HumanBeingForm getById(Long id) {

        HumanBeing hero = getEntityById(id);

        return toForm(hero);
    }

    // 4. ВНУТРЕННЕЕ ПОЛУЧЕНИЕ ENTITY ПО ID

    private HumanBeing getEntityById(Long id) {

        return humanBeingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Герой с ID " + id + " не найден"
                        )
                );
    }


    // 5. СОЗДАНИЕ ГЕРОЯ

    @Transactional
    public HumanBeingForm create(HumanBeingForm form) {

        // Создаём Entity
        HumanBeing hero = new HumanBeing();

        // Переносим данные из DTO в Entity
        fillHumanBeing(hero, form, null);

        // Repository работает только с Entity
        HumanBeing savedHero =
                humanBeingRepository.save(hero);

        // Наружу возвращаем DTO
        return toForm(savedHero);
    }


    // 6. ОБНОВЛЕНИЕ ГЕРОЯ


    @Transactional
    public HumanBeingForm update(
            Long id,
            HumanBeingForm form
    ) {

        // Получаем Entity из БД
        HumanBeing hero = getEntityById(id);

        /*
         * Передаём id текущего героя.
         * текущая машина самого героя не должна
         * считаться занятой другим HumanBeing.
         */
        fillHumanBeing(hero, form, id);

        // Сохраняем Entity
        HumanBeing savedHero =
                humanBeingRepository.save(hero);

        // Возвращаем DTO
        return toForm(savedHero);
    }



    // 7. DTO -> ENTITY

    private void fillHumanBeing(
            HumanBeing hero,
            HumanBeingForm form,
            Long currentHumanId
    ) {

        // Получаем Coordinates Entity
        Coordinates coordinates =
                coordinatesRepository.findById(
                        form.getCoordinatesId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Координаты с ID "
                                        + form.getCoordinatesId()
                                        + " не найдены"
                        )
                );


        Car car = null;

        if (form.getCarId() != null) {

            car = carRepository.findById(
                    form.getCarId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Автомобиль с ID "
                                    + form.getCarId()
                                    + " не найден"
                    )
            );



            boolean carAlreadyUsed;

            if (currentHumanId == null) {


                carAlreadyUsed =
                        humanBeingRepository.existsByCarId(
                                form.getCarId()
                        );

            } else {


                carAlreadyUsed =
                        humanBeingRepository.existsByCarIdAndIdNot(
                                form.getCarId(),
                                currentHumanId
                        );
            }


            if (carAlreadyUsed) {

                throw new IllegalStateException(
                        "Автомобиль с ID "
                                + form.getCarId()
                                + " уже принадлежит другому HumanBeing"
                );
            }
        }


        // Заполняем Entity данными из DTO


        hero.setName(form.getName());

        hero.setCoordinates(coordinates);

        hero.setRealHero(
                form.getRealHero()
        );

        hero.setHasToothpick(
                form.getHasToothpick()
        );

        hero.setCar(car);

        hero.setMood(
                form.getMood()
        );

        hero.setImpactSpeed(
                form.getImpactSpeed()
        );

        hero.setSoundtrackName(
                form.getSoundtrackName()
        );

        hero.setMinutesOfWaiting(
                form.getMinutesOfWaiting()
        );

        hero.setWeaponType(
                form.getWeaponType()
        );
    }



    // 8. ENTITY -> DTO

    private HumanBeingForm toForm(HumanBeing hero) {

        HumanBeingForm form = new HumanBeingForm();


        form.setName(
                hero.getName()
        );


        // Entity -> ID
        form.setCoordinatesId(
                hero.getCoordinates().getId()
        );


        form.setRealHero(
                hero.isRealHero()
        );


        form.setHasToothpick(
                hero.isHasToothpick()
        );


        // Car может отсутствовать
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


    // 9. УДАЛЕНИЕ ГЕРОЯ


    @Transactional
    public void delete(Long id) {


        HumanBeing hero = getEntityById(id);


        try {

            humanBeingRepository.delete(hero);

            humanBeingRepository.flush();

        } catch (DataIntegrityViolationException e) {

            throw new ObjectDeletionException(
                    "Невозможно удалить героя с ID "
                            + id
                            + ", поскольку с ним связаны другие объекты"
            );
        }
    }
}