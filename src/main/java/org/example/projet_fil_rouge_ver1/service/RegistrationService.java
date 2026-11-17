package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.model.Course;
import org.example.projet_fil_rouge_ver1.model.CoursePrerequisite;
import org.example.projet_fil_rouge_ver1.model.Registration;
import org.example.projet_fil_rouge_ver1.model.Student;
import org.example.projet_fil_rouge_ver1.repository.CoursePrerequisiteRepository;
import org.example.projet_fil_rouge_ver1.repository.GradeRepository;
import org.example.projet_fil_rouge_ver1.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository repo;
    private final StudentService studentService;
    private final CourseService courseService;
    private final CoursePrerequisiteRepository prerequisiteRepo;
    private final GradeRepository gradeRepo;

    @Value("${app.grade.moyenne:10.0}")
    private double moyenneMinimale;

    public RegistrationService(RegistrationRepository repo, StudentService studentService,
                                CourseService courseService,
                                CoursePrerequisiteRepository prerequisiteRepo,
                                GradeRepository gradeRepo) {
        this.repo = repo;
        this.studentService = studentService;
        this.courseService = courseService;
        this.prerequisiteRepo = prerequisiteRepo;
        this.gradeRepo = gradeRepo;
    }

    public Registration register(Long studentId, Long courseId) {
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseId);

        // Règle 1 : capacité
        long enrolled = repo.countByCourseIdAndStatus(courseId, Registration.Status.ACTIVE);
        if (enrolled >= course.getCapacity()) {
            throw new RuntimeException("Course is full: " + course.getCode());
        }

        // Règle 2 : prérequis pédagogiques
        checkPrerequisites(studentId, courseId);

        Registration r = new Registration();
        r.setStudent(student);
        r.setCourse(course);
        return repo.save(r);
    }

    private void checkPrerequisites(Long studentId, Long courseId) {
        List<CoursePrerequisite> prerequisites = prerequisiteRepo.findByCourseId(courseId);

        for (CoursePrerequisite prereq : prerequisites) {
            Long prereqCourseId = prereq.getPrerequisite().getId();
            String prereqCode = prereq.getPrerequisite().getCode();

            // Vérifier si l'étudiant a une inscription VALIDE pour ce prérequis
            boolean validatedByRegistration = repo.findByStudentId(studentId).stream()
                .anyMatch(r -> r.getCourse().getId().equals(prereqCourseId)
                            && r.getResult() == Registration.Result.VALIDE);

            if (validatedByRegistration) continue;

            // Vérifier via les notes (meilleure note >= moyenne)
            double bestGrade = gradeRepo
                .findBestGradeByStudentAndCourse(studentId, prereqCourseId)
                .orElse(0.0);

            if (bestGrade >= moyenneMinimale) continue;

            // Cas équivalence : prérequis non validé mais équivalence autorisée → avertissement non bloquant
            if (prereq.isEquivalenceAllowed()) continue;

            // Cas prérequis absent (jamais suivi)
            boolean everEnrolled = repo.findByStudentId(studentId).stream()
                .anyMatch(r -> r.getCourse().getId().equals(prereqCourseId));

            if (!everEnrolled) {
                throw new RuntimeException("Prérequis absent : l'étudiant n'a jamais suivi le module " + prereqCode);
            }

            // Cas prérequis suivi mais non validé
            throw new RuntimeException("Prérequis non validé : note insuffisante pour le module " + prereqCode
                + " (meilleure note : " + bestGrade + "/" + moyenneMinimale + ")");
        }
    }

    public List<Registration> findByStudent(Long studentId) {
        studentService.findById(studentId);
        return repo.findByStudentId(studentId);
    }
}
