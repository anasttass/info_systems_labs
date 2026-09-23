package ru.stud.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stud.model.HumanBeing;
import ru.stud.repository.HumanBeingRepository;

import java.util.List;

@Service
public class HumanBeingService {

    private final HumanBeingRepository repository;

    public HumanBeingService(HumanBeingRepository repository) {
        this.repository = repository;
    }

    // Получение всех героев
    @Transactional(readOnly = true)
    public List<HumanBeing> getAll() {
        return repository.findAll();
    }

    // Получение героев с пагинацией
    @Transactional(readOnly = true)
    public Page<HumanBeing> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Получение героя по ID
    @Transactional(readOnly = true)
    public HumanBeing getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Герой с ID " + id + " не найден"
                        )
                );
    }

    // Создание героя
    @Transactional
    public HumanBeing create(HumanBeing humanBeing) {

        humanBeing.setId(null);

        return repository.save(humanBeing);
    }

    // Обновление героя
    @Transactional
    public HumanBeing update(Long id, HumanBeing updated) {

        HumanBeing existing = getById(id);

        existing.setName(updated.getName());

        existing.setCoordinates(updated.getCoordinates());

        existing.setRealHero(updated.isRealHero());

        existing.setHasToothpick(updated.isHasToothpick());

        existing.setCar(updated.getCar());

        existing.setMood(updated.getMood());

        existing.setImpactSpeed(updated.getImpactSpeed());

        existing.setSoundtrackName(updated.getSoundtrackName());

        existing.setMinutesOfWaiting(
                updated.getMinutesOfWaiting()
        );

        existing.setWeaponType(updated.getWeaponType());

        return repository.save(existing);
    }

    // Удаление героя
    @Transactional
    public void delete(Long id) {

        HumanBeing humanBeing = getById(id);

        repository.delete(humanBeing);

        repository.flush();
    }
}