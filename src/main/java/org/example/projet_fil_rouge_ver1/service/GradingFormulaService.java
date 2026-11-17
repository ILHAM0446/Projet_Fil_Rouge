package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.dto.GradingFormulaRequest;
import org.example.projet_fil_rouge_ver1.model.Exam;
import org.example.projet_fil_rouge_ver1.model.GradingFormula;
import org.example.projet_fil_rouge_ver1.repository.GradingFormulaRepository;
import org.example.projet_fil_rouge_ver1.repository.GradeRepository;
import org.springframework.stereotype.Service;

@Service
public class GradingFormulaService {

    private final GradingFormulaRepository formulaRepo;
    private final GradeRepository gradeRepo;
    private final CourseService courseService;

    public GradingFormulaService(GradingFormulaRepository formulaRepo,
                                  GradeRepository gradeRepo,
                                  CourseService courseService) {
        this.formulaRepo = formulaRepo;
        this.gradeRepo = gradeRepo;
        this.courseService = courseService;
    }

    public GradingFormula save(GradingFormulaRequest req) {
        double total = req.weightCC + req.weightExam + req.weightProjet;
        if (Math.abs(total - 100.0) > 0.01) {
            throw new RuntimeException("Les pondérations doivent totaliser 100 (actuel : " + total + ")");
        }
        GradingFormula formula = formulaRepo.findByCourseId(req.courseId)
            .orElse(new GradingFormula());
        formula.setCourse(courseService.findById(req.courseId));
        formula.setWeightCC(req.weightCC);
        formula.setWeightExam(req.weightExam);
        formula.setWeightProjet(req.weightProjet);
        return formulaRepo.save(formula);
    }

    public GradingFormula findByCourse(Long courseId) {
        return formulaRepo.findByCourseId(courseId).orElse(defaultFormula(courseId));
    }

    // Calcule la moyenne pondérée d'un étudiant pour un cours
    public double calculerMoyenne(Long studentId, Long courseId) {
        GradingFormula formula = formulaRepo.findByCourseId(courseId)
            .orElse(defaultFormula(courseId));

        double avgCC     = gradeRepo.findAvgByStudentCourseAndType(studentId, courseId, Exam.EvaluationType.CC).orElse(0.0);
        double avgExam   = gradeRepo.findAvgByStudentCourseAndType(studentId, courseId, Exam.EvaluationType.EXAM).orElse(0.0);
        double avgProjet = gradeRepo.findAvgByStudentCourseAndType(studentId, courseId, Exam.EvaluationType.PROJET).orElse(0.0);

        return (avgCC * formula.getWeightCC()
              + avgExam * formula.getWeightExam()
              + avgProjet * formula.getWeightProjet()) / 100.0;
    }

    private GradingFormula defaultFormula(Long courseId) {
        GradingFormula f = new GradingFormula();
        // formule standard : CC 30%, EXAM 50%, PROJET 20%
        f.setWeightCC(30.0);
        f.setWeightExam(50.0);
        f.setWeightProjet(20.0);
        return f;
    }
}
