package org.example.projet_fil_rouge_ver1.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exams")
public class Exam {

    public enum EvaluationType { CC, EXAM, PROJET }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private EvaluationType evaluationType = EvaluationType.EXAM;

    public Exam() {}

    public Long getId() { return id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public EvaluationType getEvaluationType() { return evaluationType; }
    public void setEvaluationType(EvaluationType evaluationType) { this.evaluationType = evaluationType; }
}
