package org.example.projet_fil_rouge_ver1.service;

import org.example.projet_fil_rouge_ver1.model.Student;
import org.example.projet_fil_rouge_ver1.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    public List<Student> findAll() { return repo.findAll(); }

    public Student findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Student not found: " + id));
    }

    public Student create(Student s) { return repo.save(s); }

    public Student update(Long id, Student data) {
        Student s = findById(id);
        s.setName(data.getName());
        s.setEmail(data.getEmail());
        s.setProgram(data.getProgram());
        return repo.save(s);
    }

    public void delete(Long id) { repo.deleteById(id); }
}
