package org.example.projet_fil_rouge_ver1.repository;

import org.example.projet_fil_rouge_ver1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {}
