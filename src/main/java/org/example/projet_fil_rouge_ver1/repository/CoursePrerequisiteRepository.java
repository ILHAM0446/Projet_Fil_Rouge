package org.example.projet_fil_rouge_ver1.repository;

import org.example.projet_fil_rouge_ver1.model.CoursePrerequisite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoursePrerequisiteRepository extends JpaRepository<CoursePrerequisite, Long> {
    List<CoursePrerequisite> findByCourseId(Long courseId);
    void deleteByCourseIdAndPrerequisiteId(Long courseId, Long prerequisiteId);
}
