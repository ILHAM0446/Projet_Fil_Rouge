package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.dto.GradeRequest;
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

    @Value("${app.grade.moyenne:10.0}")
    private double moyenneMinimale;

    public GradeService(GradeRepository repo, ExamService examService,
                        StudentService studentService,
                        RegistrationRepository registrationRepo,
                        GradingFormulaService formulaService) {
        this.repo = repo;
        this.examService = examService;
        this.studentService = studentService;
        this.registrationRepo = registrationRepo;
        this.formulaService = formulaService;
    }

    public Grade save(GradeRequest req) {
        Grade g = new Grade();
        g.setExam(examService.findById(req.examId));
        g.setStudent(studentService.findById(req.studentId));
        g.setValue(req.value);
        Grade saved = repo.save(g);

        // Recalcul de la moyenne pondérée après chaque saisie de note
        Long courseId = saved.getExam().getCourse().getId();
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

    // Exposer la moyenne pondérée calculée pour un étudiant/cours
    public double getMoyennePonderee(Long studentId, Long courseId) {
        studentService.findById(studentId);
        courseId = courseId; // validation implicite via formulaService
        return formulaService.calculerMoyenne(studentId, courseId);
    }
}
