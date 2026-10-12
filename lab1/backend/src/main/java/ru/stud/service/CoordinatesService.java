package ru.stud.service;

import org.springframework.context.ApplicationEventPublisher;
import ru.stud.dto.CoordinatesForm;

import ru.stud.dto.ErrorCode;
import ru.stud.event.HumanBeingChangedEvent;
import ru.stud.exception.ResourceNotFoundException;

import ru.stud.model.Coordinates;

import ru.stud.repository.CoordinatesRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stud.util.CoordinatesMapper;

import java.util.List;


@Service
public class CoordinatesService {

    private final CoordinatesRepository repository;
    private final ApplicationEventPublisher eventPublisher;
    private final CoordinatesMapper coordinatesMapper;


    public CoordinatesService(CoordinatesRepository repository, ApplicationEventPublisher eventPublisher, CoordinatesMapper coordinatesMapper) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.coordinatesMapper = coordinatesMapper;
    }



    @Transactional(readOnly = true)
    public List<CoordinatesForm> getAll() {

        return repository.findAll().stream().map(this::toForm).toList();
    }


    @Transactional(readOnly = true)
    public CoordinatesForm getById(Long id) {

        return toForm(getEntityById(id));
    }


    private Coordinates getEntityById(Long id) {

        return repository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.COORDINATES_NOT_FOUND,"Координаты с ID " + id + " не найдены")
                );
    }


    @Transactional
    public CoordinatesForm create(CoordinatesForm form) {

        Coordinates coordinates = new Coordinates();
        fillCoordinates(form);

        Coordinates savedCoordinates = repository.save(coordinates);
        eventPublisher.publishEvent(new HumanBeingChangedEvent());
        return toForm(savedCoordinates);
    }


    @Transactional
    public CoordinatesForm update(Long id, CoordinatesForm form) {

        Coordinates existingCoordinates = getEntityById(id);
        fillCoordinates(form);
        eventPublisher.publishEvent(new HumanBeingChangedEvent());

        return toForm(existingCoordinates);
    }


    @Transactional
    public void delete(Long id) {

        Coordinates coordinates = getEntityById(id);
        repository.delete(coordinates);
        eventPublisher.publishEvent(new HumanBeingChangedEvent());

        repository.flush();
    }


    private void fillCoordinates(CoordinatesForm form) {
       coordinatesMapper.toEntity(form);
    }


    private CoordinatesForm toForm(Coordinates coordinates) {
        return coordinatesMapper.toForm( coordinates);
    }
}