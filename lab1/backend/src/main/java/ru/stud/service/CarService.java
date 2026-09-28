package ru.stud.service;

import ru.stud.dto.CarForm;

import ru.stud.exception.ObjectDeletionException;
import ru.stud.exception.ResourceNotFoundException;

import ru.stud.model.Car;

import ru.stud.repository.CarRepository;
import ru.stud.repository.HumanBeingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CarService {

    private final CarRepository repository;
    private final HumanBeingRepository humanBeingRepository;


    public CarService(
            CarRepository repository,
            HumanBeingRepository humanBeingRepository
    ) {
        this.repository = repository;
        this.humanBeingRepository = humanBeingRepository;
    }


    // 1. ПОЛУЧИТЬ ВСЕ АВТОМОБИЛИ


    @Transactional(readOnly = true)
    public List<CarForm> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toForm)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<CarForm> getAll(Pageable pageable) {

        return repository.findAll(pageable)
                .map(this::toForm);
    }



    @Transactional(readOnly = true)
    public CarForm getById(Long id) {

        return toForm(
                getEntityById(id)
        );
    }

    // 4. ПОЛУЧИТЬ ENTITY ПО ID


    private Car getEntityById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Автомобиль с ID "
                                        + id
                                        + " не найден"
                        )
                );
    }



    // 5. СОЗДАТЬ АВТОМОБИЛЬ


    @Transactional
    public CarForm create(CarForm form) {

        Car car = new Car();

        fillCar(car, form);

        Car savedCar = repository.save(car);

        return toForm(savedCar);
    }


    // 6. ОБНОВИТЬ АВТОМОБИЛЬ


    @Transactional
    public CarForm update(
            Long id,
            CarForm form
    ) {

        Car existingCar = getEntityById(id);

        fillCar(existingCar, form);

        Car savedCar = repository.save(existingCar);

        return toForm(savedCar);
    }


    // 7. УДАЛИТЬ АВТОМОБИЛЬ

    @Transactional
    public void delete(Long id) {

        Car car = getEntityById(id);

        /*
         * Нельзя удалить автомобиль,
         * если он используется HumanBeing.
         */
        if (humanBeingRepository.existsByCarId(id)) {

            throw new ObjectDeletionException(
                    "Невозможно удалить автомобиль с ID "
                            + id
                            + ": он принадлежит HumanBeing"
            );
        }

        repository.delete(car);

        repository.flush();
    }


    // 8. ПОЛУЧИТЬ СВОБОДНЫЕ АВТОМОБИЛИ


    @Transactional(readOnly = true)
    public List<CarForm> getAvailableCars() {

        return repository.findAll()
                .stream()
                .filter(car ->
                        !humanBeingRepository.existsByCarId(
                                car.getId()
                        )
                )
                .map(this::toForm)
                .toList();
    }


    // 9. DTO -> ENTITY


    private void fillCar(
            Car car,
            CarForm form
    ) {

        car.setCool(
                form.getCool()
        );
    }


    // 10. ENTITY -> DTO


    private CarForm toForm(Car car) {

        CarForm form = new CarForm();

        form.setId(
                car.getId()
        );

        form.setCool(
                car.getCool()
        );

        return form;
    }
}