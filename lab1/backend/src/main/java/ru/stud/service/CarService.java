package ru.stud.service;

import org.springframework.context.ApplicationEventPublisher;
import ru.stud.dto.CarForm;

import ru.stud.dto.ErrorCode;
import ru.stud.event.HumanBeingChangedEvent;
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
    private final ApplicationEventPublisher eventPublisher;


    public CarService(CarRepository repository, HumanBeingRepository humanBeingRepository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.humanBeingRepository = humanBeingRepository;
        this.eventPublisher = eventPublisher;
    }


    @Transactional(readOnly = true)
    public List<CarForm> getAll() {
        return repository.findAll().stream().map(this::toForm).toList();
    }

    @Transactional(readOnly = true)
    public Page<CarForm> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toForm);
    }


    @Transactional(readOnly = true)
    public CarForm getById(Long id) {
        return toForm(getEntityById(id));
    }



    private Car getEntityById(Long id) {

        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CAR_NOT_FOUND,"Автомобиль с ID " + id + " не найден"));
    }


    @Transactional
    public CarForm create(CarForm form) {

        Car car = new Car();
        fillCar(car, form);
        Car savedCar = repository.save(car);
        eventPublisher.publishEvent(
                new HumanBeingChangedEvent()
        );
        return toForm(savedCar);
    }


    @Transactional
    public CarForm update(Long id, CarForm form) {

        Car existingCar = getEntityById(id);

        fillCar(existingCar, form);

        eventPublisher.publishEvent(
                new HumanBeingChangedEvent()
        );

        return toForm(existingCar);
    }


    @Transactional
    public void delete(Long id) {

        Car car = getEntityById(id);
        if (humanBeingRepository.existsByCarId(id)) {
            throw new ObjectDeletionException(ErrorCode.CAR_DELETE_FORBIDDEN,"Невозможно удалить автомобиль с ID " + id + ": он принадлежит HumanBeing");
        }
        eventPublisher.publishEvent(
                new HumanBeingChangedEvent()
        );
        repository.delete(car);
        repository.flush();
    }

    @Transactional(readOnly = true)
    public List<CarForm> getAvailableCars() {

        return repository.findAll().stream().filter(car -> !humanBeingRepository.existsByCarId(car.getId())).map(this::toForm).toList();
    }


    //DTO -> ENTITY
    private void fillCar(Car car, CarForm form) {
        car.setCool(form.getCool());
    }

    // ENTITY -> DTO
    private CarForm toForm(Car car) {
        CarForm form = new CarForm();
        form.setId(car.getId());
        form.setCool(car.getCool());

        return form;
    }
}