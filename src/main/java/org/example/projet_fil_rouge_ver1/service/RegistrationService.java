package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.model.Course;
import org.example.projet_fil_rouge_ver1.model.CoursePrerequisite;
import org.example.projet_fil_rouge_ver1.model.Registration;
import org.example.projet_fil_rouge_ver1.model.Student;
import org.example.projet_fil_rouge_ver1.repository.CoursePrerequisiteRepository;
import org.example.projet_fil_rouge_ver1.repository.GradeRepository;
import org.example.projet_fil_rouge_ver1.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Registration register(Long studentId, Long courseId) {
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseId);

        // Vérifier doublon
        repo.findByStudentIdAndCourseId(studentId, courseId).ifPresent(r -> {
            throw new RuntimeException("Étudiant déjà inscrit ou en liste d'attente pour ce module");
        });

        // Vérifier prérequis
        checkPrerequisites(studentId, courseId);

        // Verrouillage pessimiste : lecture + comptage atomique
        long enrolled = repo.countActiveWithLock(courseId);

        Registration r = new Registration();
        r.setStudent(student);
        r.setCourse(course);

        if (enrolled >= course.getCapacity()) {
            // Module complet → liste d'attente
            int position = repo.nextWaitingPosition(courseId);
            r.setStatus(Registration.Status.WAITING_LIST);
            r.setWaitingPosition(position);
        } else {
            r.setStatus(Registration.Status.ACTIVE);
        }

        try {
            return repo.save(r);
        } catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("Conflit d'inscription concurrent, veuillez réessayer");
        }
    }

    @Transactional
    public Registration cancel(Long registrationId) {
        Registration r = repo.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Inscription introuvable : " + registrationId));

        if (r.getStatus() == Registration.Status.CANCELLED) {
            throw new RuntimeException("Inscription déjà annulée");
        }

        boolean wasActive = r.getStatus() == Registration.Status.ACTIVE;
        r.setStatus(Registration.Status.CANCELLED);
        r.setWaitingPosition(null);
        repo.save(r);

        // Promouvoir le premier en liste d'attente si une place ACTIVE se libère
        if (wasActive) {
            promouvoirPremierEnAttente(r.getCourse().getId());
        }

        return r;
    }

    // Atomique : promotion du premier étudiant en attente
    private void promouvoirPremierEnAttente(Long courseId) {
        List<Registration> waitingList = repo.findWaitingListByCourseOrdered(courseId);
        if (waitingList.isEmpty()) return;

        Registration premier = waitingList.get(0);
        premier.setStatus(Registration.Status.ACTIVE);
        premier.setWaitingPosition(null);
        repo.save(premier);

        // Réordonner les positions restantes
        List<Registration> reste = waitingList.subList(1, waitingList.size());
        for (int i = 0; i < reste.size(); i++) {
            reste.get(i).setWaitingPosition(i + 1);
            repo.save(reste.get(i));
        }
    }

    public List<Registration> findByStudent(Long studentId) {
        studentService.findById(studentId);
        return repo.findByStudentId(studentId);
    }

    public List<Registration> getWaitingList(Long courseId) {
        courseService.findById(courseId);
        return repo.findWaitingListByCourseOrdered(courseId);
    }

    private void checkPrerequisites(Long studentId, Long courseId) {
        List<CoursePrerequisite> prerequisites = prerequisiteRepo.findByCourseId(courseId);
        for (CoursePrerequisite prereq : prerequisites) {
            Long prereqCourseId = prereq.getPrerequisite().getId();
            String prereqCode = prereq.getPrerequisite().getCode();

            boolean validatedByRegistration = repo.findByStudentId(studentId).stream()
                .anyMatch(r -> r.getCourse().getId().equals(prereqCourseId)
                            && r.getResult() == Registration.Result.VALIDE);
            if (validatedByRegistration) continue;

            double bestGrade = gradeRepo
                .findBestGradeByStudentAndCourse(studentId, prereqCourseId)
                .orElse(0.0);
            if (bestGrade >= moyenneMinimale) continue;

            if (prereq.isEquivalenceAllowed()) continue;

            boolean everEnrolled = repo.findByStudentId(studentId).stream()
                .anyMatch(r -> r.getCourse().getId().equals(prereqCourseId));

            if (!everEnrolled) {
                throw new RuntimeException("Prérequis absent : l'étudiant n'a jamais suivi le module " + prereqCode);
            }
            throw new RuntimeException("Prérequis non validé : note insuffisante pour le module " + prereqCode
                + " (meilleure note : " + bestGrade + "/" + moyenneMinimale + ")");
        }
    }
}
