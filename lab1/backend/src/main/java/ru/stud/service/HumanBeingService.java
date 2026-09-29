package ru.stud.service;

import ru.stud.dto.HumanBeingForm;

import ru.stud.dto.HumanBeingResponse;
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


    @Transactional(readOnly = true)
    public List<HumanBeingResponse> getAll() {

        return humanBeingRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<HumanBeingResponse> getAll(
            Pageable pageable,
            Boolean realHero,
            Boolean hasToothpick,
            Mood mood,
            WeaponType weaponType
    ) {

        return humanBeingRepository.findAllWithFilters(realHero, hasToothpick, mood, weaponType, pageable).map(this::toResponse);
    }


    @Transactional(readOnly = true)
    public HumanBeingResponse getById(Long id) {
        HumanBeing hero = getEntityById(id);
        return toResponse(hero);
    }


    private HumanBeing getEntityById(Long id) {

        return humanBeingRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Герой с ID " + id + " не найден")
                );
    }


    @Transactional
    public HumanBeingForm create(HumanBeingForm form) {

        //Создаём Entity
        HumanBeing hero = new HumanBeing();

        //Переносим данные из DTO в Entity
        fillHumanBeing(hero, form, null);

        //Repository работает только с Entity
        HumanBeing savedHero = humanBeingRepository.save(hero);

        //Наружу возвращаем DTO
        return toForm(savedHero);
    }


    @Transactional
    public HumanBeingForm update(Long id, HumanBeingForm form) {

        // Получаем Entity из БД
        HumanBeing hero = getEntityById(id);

        fillHumanBeing(hero, form, id);

        // Сохраняем Entity
        HumanBeing savedHero = humanBeingRepository.save(hero);

        // Возвращаем DTO
        return toForm(savedHero);
    }



    private void fillHumanBeing(HumanBeing hero, HumanBeingForm form, Long currentHumanId) {

        Coordinates coordinates = coordinatesRepository.findById(form.getCoordinatesId()).orElseThrow(() ->
                        new ResourceNotFoundException("Координаты с ID " + form.getCoordinatesId() + " не найдены")
                );

        Car car = null;

        if (form.getCarId() != null) {
            car = carRepository.findById(form.getCarId()).orElseThrow(() ->
                    new ResourceNotFoundException("Автомобиль с ID " + form.getCarId() + " не найден"
                    )
            );

            boolean carAlreadyUsed;
            if (currentHumanId == null) {
                carAlreadyUsed = humanBeingRepository.existsByCarId(form.getCarId());
            } else {
                carAlreadyUsed = humanBeingRepository.existsByCarIdAndIdNot(form.getCarId(), currentHumanId);
            }

            if (carAlreadyUsed) {

                throw new IllegalStateException("Автомобиль с ID " + form.getCarId() + " уже принадлежит другому HumanBeing");
            }
        }


        //Заполняем Entity данными из DTO
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



    //ENTITY -> DTO

    private HumanBeingForm toForm(HumanBeing hero) {

        HumanBeingForm form = new HumanBeingForm();

        form.setName(hero.getName());


        // Entity -> ID
        form.setCoordinatesId(hero.getCoordinates().getId());
        form.setRealHero(hero.isRealHero());
        form.setHasToothpick(hero.isHasToothpick());


        // Car может отсутствовать
        if (hero.getCar() != null) {
            form.setCarId(hero.getCar().getId());
        }

        form.setMood(hero.getMood());

        form.setImpactSpeed(hero.getImpactSpeed());

        form.setSoundtrackName(hero.getSoundtrackName());

        form.setMinutesOfWaiting(hero.getMinutesOfWaiting());

        form.setWeaponType(hero.getWeaponType());

        return form;
    }


    @Transactional
    public void delete(Long id) {

        HumanBeing hero = getEntityById(id);

        try {

            humanBeingRepository.delete(hero);
            humanBeingRepository.flush();

        } catch (DataIntegrityViolationException e) {

            throw new ObjectDeletionException("Невозможно удалить героя с ID " + id + ", поскольку с ним связаны другие объекты");
        }
    }
    private HumanBeingResponse toResponse(HumanBeing humanBeing) {

        HumanBeingResponse response = new HumanBeingResponse();

        response.setId(humanBeing.getId());
        response.setName(humanBeing.getName());
        response.setCoordinates(humanBeing.getCoordinates());
        response.setCreationDate(humanBeing.getCreationDate());
        response.setRealHero(humanBeing.isRealHero());
        response.setHasToothpick(humanBeing.isHasToothpick());
        response.setCar(humanBeing.getCar());
        response.setMood(humanBeing.getMood());
        response.setImpactSpeed(humanBeing.getImpactSpeed());
        response.setSoundtrackName(humanBeing.getSoundtrackName());
        response.setMinutesOfWaiting(humanBeing.getMinutesOfWaiting());
        response.setWeaponType(humanBeing.getWeaponType());

        return response;
    }
}