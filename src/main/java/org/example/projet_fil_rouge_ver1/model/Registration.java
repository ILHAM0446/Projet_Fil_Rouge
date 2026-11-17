package org.example.projet_fil_rouge_ver1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "registrations", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    private Result result = Result.EN_COURS;

    public enum Status { ACTIVE, CANCELLED }
    public enum Result { EN_COURS, VALIDE, ECHEC }

    public Registration() {}

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }
}
