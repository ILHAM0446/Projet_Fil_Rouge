package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.dto.AbsenceRequest;
import org.example.projet_fil_rouge_ver1.model.Absence;
import org.example.projet_fil_rouge_ver1.model.Registration;
import org.example.projet_fil_rouge_ver1.repository.AbsenceRepository;
import org.example.projet_fil_rouge_ver1.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AbsenceService {

    private final AbsenceRepository absenceRepo;
    private final RegistrationRepository registrationRepo;
    private final StudentService studentService;
    private final CourseService courseService;

    @Value("${app.absence.seuil:3}")
    private int seuilAbsences;

    public AbsenceService(AbsenceRepository absenceRepo, RegistrationRepository registrationRepo,
                          StudentService studentService, CourseService courseService) {
        this.absenceRepo = absenceRepo;
        this.registrationRepo = registrationRepo;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public Absence enregistrer(AbsenceRequest req) {
        studentService.findById(req.studentId);
        courseService.findById(req.courseId);

        Absence a = new Absence();
        a.setStudent(studentService.findById(req.studentId));
        a.setCourse(courseService.findById(req.courseId));
        a.setDate(req.date);
        a.setJustified(req.justified);
        Absence saved = absenceRepo.save(a);

        // Règle : si absences non justifiées >= seuil → ECHEC
        long nbAbsences = absenceRepo.countByStudentIdAndCourseId(req.studentId, req.courseId);
        if (nbAbsences >= seuilAbsences) {
            registrationRepo.findByStudentId(req.studentId).stream()
                .filter(r -> r.getCourse().getId().equals(req.courseId)
                          && r.getStatus() == Registration.Status.ACTIVE)
                .findFirst()
                .ifPresent(r -> {
                    r.setResult(Registration.Result.ECHEC);
                    r.setStatus(Registration.Status.CANCELLED);
                    registrationRepo.save(r);
                });
        }

        return saved;
    }

    public List<Absence> findByStudent(Long studentId) {
        studentService.findById(studentId);
        return absenceRepo.findByStudentId(studentId);
    }

    public List<Absence> findByStudentAndCourse(Long studentId, Long courseId) {
        return absenceRepo.findByStudentIdAndCourseId(studentId, courseId);
    }
}
