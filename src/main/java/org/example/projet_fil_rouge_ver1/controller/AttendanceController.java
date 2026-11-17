package org.example.projet_fil_rouge_ver1.controller;

import jakarta.validation.Valid;
import org.example.projet_fil_rouge_ver1.dto.AttendanceRequest;
import org.example.projet_fil_rouge_ver1.model.Attendance;
import org.example.projet_fil_rouge_ver1.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) { this.service = service; }

    // Enseignant : enregistrer présence/absence à une séance
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Attendance enregistrer(@Valid @RequestBody AttendanceRequest req) {
        return service.enregistrer(req);
    }

    // Consulter les présences d'un étudiant pour un cours
    @GetMapping("/student/{studentId}/course/{courseId}")
    public List<Attendance> byStudentAndCourse(@PathVariable Long studentId,
                                                @PathVariable Long courseId) {
        return service.findByStudentAndCourse(studentId, courseId);
    }

    // Consulter le taux d'absence d'un étudiant pour un cours
    @GetMapping("/taux/student/{studentId}/course/{courseId}")
    public Map<String, Object> tauxAbsence(@PathVariable Long studentId,
                                            @PathVariable Long courseId) {
        double taux = service.getTauxAbsence(studentId, courseId);
        return Map.of(
            "studentId", studentId,
            "courseId", courseId,
            "tauxAbsence", String.format("%.1f%%", taux),
            "eligible", taux < 20.0
        );
    }

    // Vérifier explicitement l'éligibilité à l'examen final
    @GetMapping("/eligibilite/student/{studentId}/course/{courseId}")
    public Map<String, Object> verifierEligibilite(@PathVariable Long studentId,
                                                    @PathVariable Long courseId) {
        double taux = service.getTauxAbsence(studentId, courseId);
        boolean eligible = taux < 20.0;
        return Map.of(
            "eligible", eligible,
            "tauxAbsence", String.format("%.1f%%", taux),
            "message", eligible ? "Étudiant éligible à l'examen final" : "Étudiant non éligible : taux d'absence >= 20%"
        );
    }
}
