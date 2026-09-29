package ru.stud.service;

import ru.stud.dto.CoordinatesForm;

import ru.stud.dto.ErrorCode;
import ru.stud.exception.ResourceNotFoundException;

import ru.stud.model.Coordinates;

import ru.stud.repository.CoordinatesRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CoordinatesService {

    private final CoordinatesRepository repository;


    public CoordinatesService(CoordinatesRepository repository) {
        this.repository = repository;
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
        fillCoordinates(coordinates, form);

        Coordinates savedCoordinates = repository.save(coordinates);
        return toForm(savedCoordinates);
    }


    @Transactional
    public CoordinatesForm update(Long id, CoordinatesForm form) {

        Coordinates existingCoordinates = getEntityById(id);
        fillCoordinates(existingCoordinates, form);
        Coordinates savedCoordinates = repository.save(existingCoordinates);

        return toForm(savedCoordinates);
    }


    @Transactional
    public void delete(Long id) {

        Coordinates coordinates = getEntityById(id);
        repository.delete(coordinates);

        repository.flush();
    }


    private void fillCoordinates(Coordinates coordinates, CoordinatesForm form) {
        coordinates.setX(form.getX());
        coordinates.setY(form.getY());
    }


    private CoordinatesForm toForm(Coordinates coordinates) {

        CoordinatesForm form = new CoordinatesForm();
        form.setId(coordinates.getId());
        form.setX(coordinates.getX());
        form.setY(coordinates.getY());

        return form;
    }
}