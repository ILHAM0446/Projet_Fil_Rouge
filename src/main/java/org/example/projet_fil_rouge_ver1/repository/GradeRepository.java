package org.example.projet_fil_rouge_ver1.repository;

import org.example.projet_fil_rouge_ver1.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByExamId(Long examId);

    // Meilleure note d'un étudiant pour un cours donné (via exam -> course)
    @Query("SELECT MAX(g.value) FROM Grade g WHERE g.student.id = :studentId AND g.exam.course.id = :courseId")
    Optional<Double> findBestGradeByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
