package org.example.projet_fil_rouge_ver1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course_prerequisites",
       uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "prerequisite_id"}))
public class CoursePrerequisite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prerequisite_id")
    private Course prerequisite;

    // Si true, ce prérequis peut être remplacé par un autre (équivalence)
    private boolean equivalenceAllowed = false;

    public CoursePrerequisite() {}

    public Long getId() { return id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Course getPrerequisite() { return prerequisite; }
    public void setPrerequisite(Course prerequisite) { this.prerequisite = prerequisite; }
    public boolean isEquivalenceAllowed() { return equivalenceAllowed; }
    public void setEquivalenceAllowed(boolean equivalenceAllowed) { this.equivalenceAllowed = equivalenceAllowed; }
}
