package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.dto.PrerequisiteRequest;
import org.example.projet_fil_rouge_ver1.model.CoursePrerequisite;
import org.example.projet_fil_rouge_ver1.repository.CoursePrerequisiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CoursePrerequisiteService {

    private final CoursePrerequisiteRepository repo;
    private final CourseService courseService;

    public CoursePrerequisiteService(CoursePrerequisiteRepository repo, CourseService courseService) {
        this.repo = repo;
        this.courseService = courseService;
    }

    public CoursePrerequisite add(PrerequisiteRequest req) {
        if (req.courseId.equals(req.prerequisiteId)) {
            throw new RuntimeException("Un cours ne peut pas être son propre prérequis");
        }
        CoursePrerequisite cp = new CoursePrerequisite();
        cp.setCourse(courseService.findById(req.courseId));
        cp.setPrerequisite(courseService.findById(req.prerequisiteId));
        cp.setEquivalenceAllowed(req.equivalenceAllowed);
        return repo.save(cp);
    }

    public List<CoursePrerequisite> findByCourse(Long courseId) {
        return repo.findByCourseId(courseId);
    }

    @Transactional
    public void remove(Long courseId, Long prerequisiteId) {
        repo.deleteByCourseIdAndPrerequisiteId(courseId, prerequisiteId);
    }
}
