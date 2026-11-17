package org.example.projet_fil_rouge_ver1.controller;

import jakarta.validation.Valid;
import org.example.projet_fil_rouge_ver1.dto.RegistrationRequest;
import org.example.projet_fil_rouge_ver1.model.Registration;
import org.example.projet_fil_rouge_ver1.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) { this.service = service; }

    // Inscrire ou placer en liste d'attente
    @PostMapping("/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public Registration register(@Valid @RequestBody RegistrationRequest req) {
        return service.register(req.studentId, req.courseId);
    }

    // Annuler une inscription (déclenche promotion automatique)
    @PatchMapping("/registrations/{id}/cancel")
    public Registration cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

    // Consulter les inscriptions d'un étudiant
    @GetMapping("/students/{id}/registrations")
    public List<Registration> getByStudent(@PathVariable Long id) {
        return service.findByStudent(id);
    }

    // Consulter la liste d'attente d'un cours
    @GetMapping("/courses/{courseId}/waiting-list")
    public List<Registration> getWaitingList(@PathVariable Long courseId) {
        return service.getWaitingList(courseId);
    }
}
