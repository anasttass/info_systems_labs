package ru.stud.service;

import ru.stud.dto.HumanBeingForm;

import ru.stud.model.*;

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


    // ==========================================
    // 1. ПОЛУЧЕНИЕ ВСЕХ ГЕРОЕВ
    // ==========================================

    @Transactional(readOnly = true)
    public List<HumanBeing> getAll() {

        return humanBeingRepository.findAll();
    }


    // ==========================================
    // 2. ПОЛУЧЕНИЕ ГЕРОЕВ С ПАГИНАЦИЕЙ
    // ==========================================

    @Transactional(readOnly = true)
    public Page<HumanBeing> getAll(Pageable pageable, Boolean realHero, Boolean hasToothpick, Mood mood, WeaponType weaponType) {

//        String normalizedName = name == null || name.isBlank() ? null : name.trim();

        return humanBeingRepository.findAllWithFilters(realHero,hasToothpick,mood,weaponType,pageable);
    }


    // ==========================================
    // 3. ПОЛУЧЕНИЕ ГЕРОЯ ПО ID
    // ==========================================

    @Transactional(readOnly = true)
    public HumanBeing getById(Long id) {

        return humanBeingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Герой с ID " + id + " не найден"
                        )
                );
    }


    // ==========================================
    // 4. СОЗДАНИЕ ГЕРОЯ
    // ==========================================

    @Transactional
    public HumanBeing create(HumanBeingForm form) {

        // Создаём новый объект

        HumanBeing hero = new HumanBeing();

        // Заполняем его данными из DTO

        fillHumanBeing(hero, form, null);

        // Сохраняем в PostgreSQL

        return humanBeingRepository.save(hero);
    }


    // ==========================================
    // 5. ОБНОВЛЕНИЕ ГЕРОЯ
    // ==========================================

    @Transactional
    public HumanBeing update(
            Long id,
            HumanBeingForm form
    ) {

        // Получаем существующего героя

        HumanBeing hero = getById(id);

        // Изменяем его атрибуты

        fillHumanBeing(hero, form, null);

        // Сохраняем изменения

        return humanBeingRepository.save(hero);
    }


    // ==========================================
    // 6. ЗАПОЛНЕНИЕ ENTITY ИЗ DTO
    // ==========================================

    private void fillHumanBeing(
            HumanBeing hero,
            HumanBeingForm form,
            Long currentHumanId
    ) {

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
                // Создание нового HumanBeing
                carAlreadyUsed =
                        humanBeingRepository.existsByCarId(
                                form.getCarId()
                        );
            } else {
                // Редактирование.
                // Своя текущая машина разрешена.
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

        hero.setName(form.getName());
        hero.setCoordinates(coordinates);
        hero.setRealHero(form.getRealHero());
        hero.setHasToothpick(form.getHasToothpick());
        hero.setCar(car);
        hero.setMood(form.getMood());
        hero.setImpactSpeed(form.getImpactSpeed());
        hero.setSoundtrackName(form.getSoundtrackName());
        hero.setMinutesOfWaiting(form.getMinutesOfWaiting());
        hero.setWeaponType(form.getWeaponType());
    }


    // ==========================================
    // 7. ПРЕОБРАЗОВАНИЕ ENTITY В DTO
    // ==========================================

    @Transactional(readOnly = true)
    public HumanBeingForm toForm(HumanBeing hero) {

        HumanBeingForm form = new HumanBeingForm();

        form.setName(hero.getName());

        form.setCoordinatesId(
                hero.getCoordinates().getId()
        );

        form.setRealHero(hero.isRealHero());

        form.setHasToothpick(
                hero.isHasToothpick()
        );

        if (hero.getCar() != null) {

            form.setCarId(
                    hero.getCar().getId()
            );
        }

        form.setMood(hero.getMood());

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


    // ==========================================
    // 8. УДАЛЕНИЕ ГЕРОЯ
    // ==========================================

    @Transactional
    public void delete(Long id) {

        HumanBeing hero = getById(id);

        try {

            humanBeingRepository.delete(hero);

            // Принудительно выполняем SQL DELETE

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