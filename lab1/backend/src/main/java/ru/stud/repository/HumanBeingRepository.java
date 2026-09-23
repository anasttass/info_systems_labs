package ru.stud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stud.model.HumanBeing;

public interface HumanBeingRepository  extends JpaRepository<HumanBeing,Long> {

}
