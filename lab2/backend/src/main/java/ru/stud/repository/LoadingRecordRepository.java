package ru.stud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stud.model.LoadingRecord;
import java.util.List;

public interface LoadingRecordRepository extends JpaRepository<LoadingRecord, Long> {
    List<LoadingRecord> findAllByOrderByIdDesc();
    List<LoadingRecord> findByCreatorIdOrderByIdDesc(Long creatorId);
}
