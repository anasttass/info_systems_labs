package ru.stud.service;

import ru.stud.model.HumanBeing;
import ru.stud.model.Mood;
import ru.stud.repository.HumanBeingRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SpecialOperationsService {

    private final HumanBeingRepository repository;

    public SpecialOperationsService(HumanBeingRepository repository) {
        this.repository = repository;
    }

    // 1. Сгруппировать героев по soundtrackName
    // и вернуть количество элементов в каждой группе

    @Transactional(readOnly = true)
    public Map<String, Long> groupBySoundtrack() {

        List<HumanBeing> heroes = repository.findAll();

        return heroes.stream()
                .collect(Collectors.groupingBy(
                        HumanBeing::getSoundtrackName,
                        Collectors.counting()
                ));
    }

    // 2. Найти всех героев, имя которых содержит
    // заданную подстроку

    @Transactional(readOnly = true)
    public List<HumanBeing> searchByName(String substring) {

        List<HumanBeing> heroes = repository.findAll();

        return heroes.stream()
                .filter(hero ->
                        hero.getName().contains(substring)
                )
                .collect(Collectors.toList());
    }

    // 3. Получить уникальные значения minutesOfWaiting

    @Transactional(readOnly = true)
    public List<Integer> getUniqueWaitingMinutes() {

        List<HumanBeing> heroes = repository.findAll();

        return heroes.stream()
                .map(HumanBeing::getMinutesOfWaiting)
                .distinct()
                .collect(Collectors.toList());
    }

    // 4. Удалить всех героев без зубочисток

    @Transactional
    public void deleteWithoutToothpick() {

        List<HumanBeing> heroes = repository.findAll();

        List<HumanBeing> heroesToDelete = heroes.stream()
                .filter(hero -> !hero.isHasToothpick())
                .collect(Collectors.toList());

        repository.deleteAll(heroesToDelete);

        repository.flush();
    }

    // 5. Установить всем героям максимально
    // печальное настроение

    @Transactional
    public void setSadnessForAll() {

        List<HumanBeing> heroes = repository.findAll();

        for (HumanBeing hero : heroes) {
            hero.setMood(Mood.SADNESS);
        }

        repository.saveAll(heroes);
    }
}