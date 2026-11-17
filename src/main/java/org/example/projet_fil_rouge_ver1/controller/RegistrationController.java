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

    @PostMapping("/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public Registration register(@Valid @RequestBody RegistrationRequest req) {
        return service.register(req.studentId, req.courseId);
    }

    @GetMapping("/students/{id}/registrations")
    public List<Registration> getByStudent(@PathVariable Long id) {
        return service.findByStudent(id);
    }
}
