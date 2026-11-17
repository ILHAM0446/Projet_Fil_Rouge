package org.example.projet_fil_rouge_ver1.controller;

import jakarta.validation.Valid;
import org.example.projet_fil_rouge_ver1.dto.AbsenceRequest;
import org.example.projet_fil_rouge_ver1.model.Absence;
import org.example.projet_fil_rouge_ver1.service.AbsenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/absences")
public class AbsenceController {

    private final AbsenceService service;

    public AbsenceController(AbsenceService service) { this.service = service; }

    // Enseignant : enregistrer une absence
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Absence enregistrer(@Valid @RequestBody AbsenceRequest req) {
        return service.enregistrer(req);
    }

    // Étudiant / Admin / Enseignant : consulter les absences d'un étudiant
    @GetMapping("/student/{studentId}")
    public List<Absence> byStudent(@PathVariable Long studentId) {
        return service.findByStudent(studentId);
    }

    // Consulter les absences d'un étudiant pour un module précis
    @GetMapping("/student/{studentId}/course/{courseId}")
    public List<Absence> byStudentAndCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return service.findByStudentAndCourse(studentId, courseId);
    }
}
