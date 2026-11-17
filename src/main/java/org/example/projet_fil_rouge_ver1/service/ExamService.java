package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.dto.ExamRequest;
import org.example.projet_fil_rouge_ver1.model.Exam;
import org.example.projet_fil_rouge_ver1.repository.ExamRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExamService {

    private final ExamRepository repo;
    private final CourseService courseService;

    public ExamService(ExamRepository repo, CourseService courseService) {
        this.repo = repo;
        this.courseService = courseService;
    }

    public List<Exam> findAll() { return repo.findAll(); }

    public Exam findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Exam not found: " + id));
    }

    public Exam create(ExamRequest req) {
        Exam e = new Exam();
        e.setCourse(courseService.findById(req.courseId));
        e.setDate(req.date);
        return repo.save(e);
    }
}
