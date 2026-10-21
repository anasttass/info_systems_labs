package ru.stud.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stud.dto.ImportHumanForm;
import ru.stud.event.HumanBeingChangedEvent;
import ru.stud.model.*;
import ru.stud.repository.*;
import java.util.List;

@Service
public class ImportTransactionService {
    private final HumanBeingRepository humans;
    private final CoordinatesRepository coordinates;
    private final CarRepository cars;
    private final ApplicationEventPublisher events;

    public ImportTransactionService(HumanBeingRepository humans, CoordinatesRepository coordinates,
                                    CarRepository cars, ApplicationEventPublisher events) {
        this.humans = humans;
        this.coordinates = coordinates;
        this.cars = cars;
        this.events = events;
    }
// TODO: MAPPER
    @Transactional(rollbackFor = Exception.class)
    public int importAll(List<ImportHumanForm> items, User owner) {
        for (ImportHumanForm item : items) {
            Coordinates c = new Coordinates();
            c.setX(item.coordinates().getX());
            c.setY(item.coordinates().getY());
            c = coordinates.save(c);
            Car car = null;
            if (item.car() != null) {
                car = new Car();
                car.setCool(item.car().getCool());
                car = cars.save(car);
            }
            HumanBeing human = new HumanBeing();
            human.setOwner(owner);
            human.setName(item.name());
            human.setCoordinates(c);
            human.setRealHero(item.realHero());
            human.setHasToothpick(item.hasToothpick());
            human.setCar(car);
            human.setMood(item.mood());
            human.setImpactSpeed(item.impactSpeed());
            human.setSoundtrackName(item.soundtrackName());
            human.setMinutesOfWaiting(item.minutesOfWaiting());
            human.setWeaponType(item.weaponType());
            humans.save(human);
        }
        humans.flush();
        events.publishEvent(new HumanBeingChangedEvent());
        return items.size();
    }
}
