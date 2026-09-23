package ru.stud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stud.model.HumanBeing;

@Repository
public interface HumanBeingRepository
        extends JpaRepository<HumanBeing, Long> {

}
