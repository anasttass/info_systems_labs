package ru.stud.service;

import ru.stud.exception.ObjectDeletionException;
import ru.stud.exception.ResourceNotFoundException;
import ru.stud.model.Car;
import ru.stud.repository.CarRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stud.repository.HumanBeingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CarService {

    private final CarRepository repository;
    private final HumanBeingRepository humanBeingRepository;

    public CarService(CarRepository repository, HumanBeingRepository humanBeingRepository) {
        this.repository = repository;
        this.humanBeingRepository = humanBeingRepository;
    }

    @Transactional(readOnly = true)
    public List<Car> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Car> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Car getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Автомобиль с ID " + id + " не найден"
                        )
                );
    }

    @Transactional
    public Car create(Car car) {
        // ID должен генерироваться БД
        car.setId(null);

        return repository.save(car);
    }

    @Transactional
    public Car update(Long id, Car newCar) {

        // Сначала проверяем, существует ли такая машина
        Car existingCar = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Автомобиль с ID " + id + " не найден"
                        )
                );

        // Изменяем только разрешённые поля
        existingCar.setCool(newCar.getCool());

        return repository.save(existingCar);
    }

    @Transactional
    public void delete(Long id) {

        Car car = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Автомобиль с ID "
                                        + id
                                        + " не найден"
                        )
                );

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
    @Transactional(readOnly = true)
    public List<Car> getAvailableCars() {
        return repository.findAll()
                .stream()
                .filter(car ->
                        !humanBeingRepository.existsByCarId(
                                car.getId()
                        )
                )
                .toList();
    }
}