package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.model.Course;
import org.example.projet_fil_rouge_ver1.model.Registration;
import org.example.projet_fil_rouge_ver1.model.Student;
import org.example.projet_fil_rouge_ver1.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository repo;
    private final StudentService studentService;
    private final CourseService courseService;

    public RegistrationService(RegistrationRepository repo, StudentService studentService, CourseService courseService) {
        this.repo = repo;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public Registration register(Long studentId, Long courseId) {
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseId);

        long enrolled = repo.countByCourseIdAndStatus(courseId, Registration.Status.ACTIVE);
        if (enrolled >= course.getCapacity()) {
            throw new RuntimeException("Course is full: " + course.getCode());
        }

        Registration r = new Registration();
        r.setStudent(student);
        r.setCourse(course);
        return repo.save(r);
    }

    public List<Registration> findByStudent(Long studentId) {
        studentService.findById(studentId); // valide existence
        return repo.findByStudentId(studentId);
    }
}
