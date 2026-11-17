package org.example.projet_fil_rouge_ver1.repository;

import jakarta.persistence.LockModeType;
import org.example.projet_fil_rouge_ver1.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByStudentId(Long studentId);

    // Verrouillage pessimiste sur le comptage → atomicité vérification capacité
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.course.id = :courseId AND r.status = 'ACTIVE'")
    long countActiveWithLock(@Param("courseId") Long courseId);

    long countByCourseIdAndStatus(Long courseId, Registration.Status status);

    // Premier étudiant en attente pour un cours (position la plus basse)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Registration r WHERE r.course.id = :courseId AND r.status = 'WAITING_LIST' ORDER BY r.waitingPosition ASC")
    List<Registration> findWaitingListByCourseOrdered(@Param("courseId") Long courseId);

    // Prochaine position dans la liste d'attente
    @Query("SELECT COALESCE(MAX(r.waitingPosition), 0) + 1 FROM Registration r WHERE r.course.id = :courseId AND r.status = 'WAITING_LIST'")
    int nextWaitingPosition(@Param("courseId") Long courseId);

    Optional<Registration> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
