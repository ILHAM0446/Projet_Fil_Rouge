package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.dto.GradeRequest;
import org.example.projet_fil_rouge_ver1.model.Exam;
import org.example.projet_fil_rouge_ver1.model.Grade;
import org.example.projet_fil_rouge_ver1.model.Registration;
import org.example.projet_fil_rouge_ver1.repository.GradeRepository;
import org.example.projet_fil_rouge_ver1.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GradeService {

    private final GradeRepository repo;
    private final ExamService examService;
    private final StudentService studentService;
    private final RegistrationRepository registrationRepo;
    private final GradingFormulaService formulaService;
    private final AttendanceService attendanceService;

    @Value("${app.grade.moyenne:10.0}")
    private double moyenneMinimale;

    public GradeService(GradeRepository repo, ExamService examService,
                        StudentService studentService,
                        RegistrationRepository registrationRepo,
                        GradingFormulaService formulaService,
                        AttendanceService attendanceService) {
        this.repo = repo;
        this.examService = examService;
        this.studentService = studentService;
        this.registrationRepo = registrationRepo;
        this.formulaService = formulaService;
        this.attendanceService = attendanceService;
    }

    public Grade save(GradeRequest req) {
        Grade g = new Grade();
        g.setExam(examService.findById(req.examId));
        g.setStudent(studentService.findById(req.studentId));
        g.setValue(req.value);

        Long courseId = g.getExam().getCourse().getId();

        // Bloquer la saisie si examen final et absences >= 20%
        if (g.getExam().getEvaluationType() == Exam.EvaluationType.EXAM) {
            attendanceService.verifierEligibiliteExamen(req.studentId, courseId);
        }

        Grade saved = repo.save(g);

        // Recalcul de la moyenne pondérée après chaque saisie
        double moyennePonderee = formulaService.calculerMoyenne(req.studentId, courseId);

        registrationRepo.findByStudentId(req.studentId).stream()
            .filter(r -> r.getCourse().getId().equals(courseId)
                      && r.getStatus() == Registration.Status.ACTIVE)
            .findFirst()
            .ifPresent(r -> {
                r.setResult(moyennePonderee >= moyenneMinimale
                    ? Registration.Result.VALIDE
                    : Registration.Result.ECHEC);
                registrationRepo.save(r);
            });

        return saved;
    }

    public List<Grade> findByStudent(Long studentId) {
        studentService.findById(studentId);
        return repo.findByStudentId(studentId);
    }

    public List<Grade> findByExam(Long examId) {
        examService.findById(examId);
        return repo.findByExamId(examId);
    }

    public double getMoyennePonderee(Long studentId, Long courseId) {
        studentService.findById(studentId);
        return formulaService.calculerMoyenne(studentId, courseId);
    }
}
