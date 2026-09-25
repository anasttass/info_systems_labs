package ru.stud.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stud.model.HumanBeing;
import ru.stud.model.Mood;
import ru.stud.model.WeaponType;

import java.util.Optional;

@Repository
public interface HumanBeingRepository
        extends JpaRepository<HumanBeing, Long> {


    Optional<HumanBeing> findByCarId(Long carId);

    boolean existsByCarId(Long CarId);

    boolean existsByCarIdAndIdNot(
            Long carId,
            Long humanBeingId
    );

    @Query("""
            SELECT h FROM HumanBeing h
            WHERE (:realHero IS NULL OR h.realHero = :realHero)
            AND (:hasToothpick IS NULL OR h.hasToothpick = :hasToothpick)
            AND (:mood IS NULL OR h.mood=:mood)
            AND (:weaponType IS NULL OR h.weaponType=:weaponType)
            """)
    Page<HumanBeing> findAllWithFilters(
            @Param("realHero") Boolean realHero,
            @Param("hasToothpick") Boolean hasToothpick,
            @Param("mood") Mood mood,
            @Param("weaponType")WeaponType weaponType,
            Pageable pageable);
}
