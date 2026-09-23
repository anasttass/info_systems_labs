package ru.stud.service;

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
    public List<Coordinates> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Coordinates getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Координаты с ID " + id + " не найдены"
                        )
                );
    }

    @Transactional
    public Coordinates create(Coordinates coordinates) {
        return repository.save(coordinates);
    }
}