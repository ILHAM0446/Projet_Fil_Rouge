package org.example.projet_fil_rouge_ver1.controller;

import jakarta.validation.Valid;
import org.example.projet_fil_rouge_ver1.dto.GradeRequest;
import org.example.projet_fil_rouge_ver1.model.Grade;
import org.example.projet_fil_rouge_ver1.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeService service;

    public GradeController(GradeService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Grade save(@Valid @RequestBody GradeRequest req) { return service.save(req); }

    @GetMapping("/student/{studentId}")
    public List<Grade> byStudent(@PathVariable Long studentId) { return service.findByStudent(studentId); }

    @GetMapping("/exam/{examId}")
    public List<Grade> byExam(@PathVariable Long examId) { return service.findByExam(examId); }
}
