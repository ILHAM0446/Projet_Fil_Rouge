package org.example.projet_fil_rouge_ver1.repository;

import org.example.projet_fil_rouge_ver1.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId);
    long countByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, Attendance.AttendanceStatus status);
}
