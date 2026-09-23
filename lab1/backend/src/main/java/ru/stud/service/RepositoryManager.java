package ru.stud.service;

import ru.stud.model.Coordinates;
import ru.stud.model.HumanBeing;
import ru.stud.repository.CarRepository;
import ru.stud.repository.CoordinatesRepository;
import ru.stud.repository.HumanBeingRepository;

import java.util.Optional;

public class RepositoryManager {
    private HumanBeingRepository humanBeingRepository;
    private CarRepository carRepository;
    private CoordinatesRepository coordinatesRepository;

    public RepositoryManager(HumanBeingRepository humanBeingRepository, CarRepository carRepository, CoordinatesRepository coordinatesRepository){
        this.humanBeingRepository=humanBeingRepository;
        this.carRepository=carRepository;
        this.coordinatesRepository=coordinatesRepository;
    }

    public HumanBeing addHumanBeing(HumanBeing humanBeing){
        return humanBeingRepository.save(humanBeing);
    }
    public Optional<HumanBeing> getHumanBeingById(Long id){
        return humanBeingRepository.findById(id);
    }
    public HumanBeing updateHumanBeing(HumanBeing humanBeing){
        if(!humanBeingRepository.existsById(humanBeing.getId())){
            throw new RuntimeException("HumanBeing not found");
        }
        return humanBeingRepository.save(humanBeing);
    }
    public void deleteHumanBeingById(Long id){
        humanBeingRepository.deleteById(id);
    }

}

