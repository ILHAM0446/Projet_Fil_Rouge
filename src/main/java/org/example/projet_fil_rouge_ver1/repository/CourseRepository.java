package org.example.projet_fil_rouge_ver1.repository;

import org.example.projet_fil_rouge_ver1.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {}
