package org.example.projet_fil_rouge_ver1.controller;

import jakarta.validation.Valid;
import org.example.projet_fil_rouge_ver1.dto.GradingFormulaRequest;
import org.example.projet_fil_rouge_ver1.model.GradingFormula;
import org.example.projet_fil_rouge_ver1.service.GradingFormulaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grading-formulas")
public class GradingFormulaController {

    private final GradingFormulaService service;

    public GradingFormulaController(GradingFormulaService service) { this.service = service; }

    // Admin : configurer ou modifier la formule d'un cours
    @PostMapping
    public GradingFormula save(@Valid @RequestBody GradingFormulaRequest req) {
        return service.save(req);
    }

    // Consulter la formule d'un cours
    @GetMapping("/course/{courseId}")
    public GradingFormula getByCourse(@PathVariable Long courseId) {
        return service.findByCourse(courseId);
    }
}
