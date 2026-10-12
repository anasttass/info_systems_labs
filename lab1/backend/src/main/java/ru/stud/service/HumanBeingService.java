package ru.stud.service;

import ru.stud.dto.ErrorCode;
import org.springframework.context.ApplicationEventPublisher;
import ru.stud.dto.HumanBeingForm;

import ru.stud.dto.HumanBeingResponse;
import ru.stud.event.HumanBeingChangedEvent;
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
import ru.stud.websocket.HumanBeingWebSocketHandler;
import ru.stud.util.HumanBeingMapper;

import java.util.List;


@Service
public class HumanBeingService {

    private final HumanBeingRepository humanBeingRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final CarRepository carRepository;
    private final HumanBeingWebSocketHandler webSocketHandler;
    private final ApplicationEventPublisher eventPublisher;
    private final HumanBeingMapper humanBeingMapper;

    public HumanBeingService(
            HumanBeingRepository humanBeingRepository,
            CoordinatesRepository coordinatesRepository,
            CarRepository carRepository,
            HumanBeingWebSocketHandler webSocketHandler,
            ApplicationEventPublisher eventPublisher, HumanBeingMapper humanBeingMapper
    ) {
        this.humanBeingRepository = humanBeingRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.carRepository = carRepository;
        this.webSocketHandler = webSocketHandler;
        this.eventPublisher = eventPublisher;
        this.humanBeingMapper = humanBeingMapper;
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
                        new ResourceNotFoundException(ErrorCode.HUMAN_NOT_FOUND,"Герой с ID " + id + " не найден")
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
        eventPublisher.publishEvent(
                new HumanBeingChangedEvent()
        );
        //Наружу возвращаем DTO
        return toForm(savedHero);


    }


    @Transactional
    public HumanBeingForm update(Long id, HumanBeingForm form) {
        HumanBeing hero = getEntityById(id);

        fillHumanBeing(hero, form, id);

        eventPublisher.publishEvent(
                new HumanBeingChangedEvent()
        );

        return toForm(hero);
    }



    private void fillHumanBeing(HumanBeing hero, HumanBeingForm form, Long currentHumanId) {

        Coordinates coordinates = coordinatesRepository.findById(form.getCoordinatesId()).orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.COORDINATES_NOT_FOUND,"Координаты с ID " + form.getCoordinatesId() + " не найдены")
                );

        Car car = null;

        if (form.getCarId() != null) {
            car = carRepository.findById(form.getCarId()).orElseThrow(() ->
                    new ResourceNotFoundException(ErrorCode.CAR_NOT_FOUND,"Автомобиль с ID " + form.getCarId() + " не найден"
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
        humanBeingMapper.toEntity(form);

    }





    private HumanBeingForm toForm(HumanBeing hero) {
        return humanBeingMapper.toForm(hero);
    }


    @Transactional
    public void delete(Long id) {

        HumanBeing hero = getEntityById(id);

        try {

            humanBeingRepository.delete(hero);
            humanBeingRepository.flush();
            eventPublisher.publishEvent(
                    new HumanBeingChangedEvent()
            );

        } catch (DataIntegrityViolationException e) {

            throw new ObjectDeletionException(ErrorCode.HUMAN_DELETE_FORBIDDEN,"Невозможно удалить героя с ID " + id + ", поскольку с ним связаны другие объекты");
        }
    }

    private HumanBeingResponse toResponse(HumanBeing humanBeing) {

        return humanBeingMapper.toResponse(humanBeing);
    }
}