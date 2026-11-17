package org.example.projet_fil_rouge_ver1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "grades", uniqueConstraints = @UniqueConstraint(columnNames = {"exam_id", "student_id"}))
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @DecimalMin("0.0") @DecimalMax("20.0")
    private Double value;

    public Grade() {}

    public Long getId() { return id; }
    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}
