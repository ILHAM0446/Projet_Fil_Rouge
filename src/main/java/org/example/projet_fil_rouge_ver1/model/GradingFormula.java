package org.example.projet_fil_rouge_ver1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "grading_formulas")
public class GradingFormula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "course_id", unique = true)
    private Course course;

    // Pondérations en % (doivent totaliser 100)
    @DecimalMin("0.0") @DecimalMax("100.0")
    private double weightCC = 30.0;      // contrôle continu

    @DecimalMin("0.0") @DecimalMax("100.0")
    private double weightExam = 50.0;    // examen final

    @DecimalMin("0.0") @DecimalMax("100.0")
    private double weightProjet = 20.0;  // projet

    public GradingFormula() {}

    public Long getId() { return id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public double getWeightCC() { return weightCC; }
    public void setWeightCC(double weightCC) { this.weightCC = weightCC; }
    public double getWeightExam() { return weightExam; }
    public void setWeightExam(double weightExam) { this.weightExam = weightExam; }
    public double getWeightProjet() { return weightProjet; }
    public void setWeightProjet(double weightProjet) { this.weightProjet = weightProjet; }
}
