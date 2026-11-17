package org.example.projet_fil_rouge_ver1.repository;

import org.example.projet_fil_rouge_ver1.model.GradingFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GradingFormulaRepository extends JpaRepository<GradingFormula, Long> {
    Optional<GradingFormula> findByCourseId(Long courseId);
}
