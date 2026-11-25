package br.com.regulei.backend.menstrualrecord.repository;

import br.com.regulei.backend.menstrualrecord.entity.MenstrualRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenstrualRecordRepository extends JpaRepository<MenstrualRecord, Long> {
    List<MenstrualRecord> findAllByUserIdOrderByStartDateDesc(Long userId);
    List<MenstrualRecord> findAllByUserIdOrderByStartDateAsc(Long userId);
    Optional<MenstrualRecord> findByIdAndUserId(Long id, Long userId);
    boolean existsByUserIdAndEndDateIsNull(Long userId);
}
