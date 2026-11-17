package org.example.projet_fil_rouge_ver1.controller;

import jakarta.validation.Valid;
import org.example.projet_fil_rouge_ver1.dto.PrerequisiteRequest;
import org.example.projet_fil_rouge_ver1.model.CoursePrerequisite;
import org.example.projet_fil_rouge_ver1.service.CoursePrerequisiteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/prerequisites")
public class CoursePrerequisiteController {

    private final CoursePrerequisiteService service;

    public CoursePrerequisiteController(CoursePrerequisiteService service) { this.service = service; }

    // Admin : ajouter un prérequis à un cours
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoursePrerequisite add(@Valid @RequestBody PrerequisiteRequest req) {
        return service.add(req);
    }

    // Consulter les prérequis d'un cours
    @GetMapping("/course/{courseId}")
    public List<CoursePrerequisite> getByCourse(@PathVariable Long courseId) {
        return service.findByCourse(courseId);
    }

    // Admin : supprimer un prérequis
    @DeleteMapping("/course/{courseId}/prerequisite/{prerequisiteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long courseId, @PathVariable Long prerequisiteId) {
        service.remove(courseId, prerequisiteId);
    }
}
