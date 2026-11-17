package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.model.Course;
import org.example.projet_fil_rouge_ver1.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repo;

    public CourseService(CourseRepository repo) { this.repo = repo; }

    public List<Course> findAll() { return repo.findAll(); }

    public Course findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Course not found: " + id));
    }

    public Course create(Course c) { return repo.save(c); }

    public Course update(Long id, Course data) {
        Course c = findById(id);
        c.setCode(data.getCode());
        c.setTitle(data.getTitle());
        c.setCapacity(data.getCapacity());
        return repo.save(c);
    }

    public void delete(Long id) { repo.deleteById(id); }
}
