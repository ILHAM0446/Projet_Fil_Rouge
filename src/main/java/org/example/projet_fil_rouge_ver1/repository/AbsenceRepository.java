package org.example.projet_fil_rouge_ver1.repository;

import org.example.projet_fil_rouge_ver1.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByStudentId(Long studentId);
    List<Absence> findByStudentIdAndCourseId(Long studentId, Long courseId);
    long countByStudentIdAndCourseId(Long studentId, Long courseId);
}
