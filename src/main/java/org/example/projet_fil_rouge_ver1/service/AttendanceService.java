package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.dto.AttendanceRequest;
import org.example.projet_fil_rouge_ver1.model.Attendance;
import org.example.projet_fil_rouge_ver1.model.Course;
import org.example.projet_fil_rouge_ver1.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository repo;
    private final StudentService studentService;
    private final CourseService courseService;

    public AttendanceService(AttendanceRepository repo,
                              StudentService studentService,
                              CourseService courseService) {
        this.repo = repo;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public Attendance enregistrer(AttendanceRequest req) {
        Attendance a = new Attendance();
        a.setStudent(studentService.findById(req.studentId));
        a.setCourse(courseService.findById(req.courseId));
        a.setSessionDate(req.sessionDate);
        a.setStatus(req.status);
        return repo.save(a);
    }

    public List<Attendance> findByStudentAndCourse(Long studentId, Long courseId) {
        return repo.findByStudentIdAndCourseId(studentId, courseId);
    }

    // Vérifie si l'étudiant est éligible à l'examen final (absences non justifiées < 20%)
    public void verifierEligibiliteExamen(Long studentId, Long courseId) {
        Course course = courseService.findById(courseId);
        int totalSessions = course.getTotalSessions();

        long absencesNonJustifiees = repo.countByStudentIdAndCourseIdAndStatus(
            studentId, courseId, Attendance.AttendanceStatus.ABSENT);

        double tauxAbsence = (double) absencesNonJustifiees / totalSessions * 100.0;

        if (tauxAbsence >= 20.0) {
            throw new RuntimeException(
                String.format("Étudiant non éligible à l'examen : %.1f%% d'absences non justifiées (seuil : 20%%, total séances : %d)",
                    tauxAbsence, totalSessions));
        }
    }

    public double getTauxAbsence(Long studentId, Long courseId) {
        Course course = courseService.findById(courseId);
        long absences = repo.countByStudentIdAndCourseIdAndStatus(
            studentId, courseId, Attendance.AttendanceStatus.ABSENT);
        return (double) absences / course.getTotalSessions() * 100.0;
    }
}
