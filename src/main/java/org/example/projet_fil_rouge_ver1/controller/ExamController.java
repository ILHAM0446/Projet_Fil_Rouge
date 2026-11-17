package org.example.projet_fil_rouge_ver1.controller;

import jakarta.validation.Valid;
import org.example.projet_fil_rouge_ver1.dto.ExamRequest;
import org.example.projet_fil_rouge_ver1.model.Exam;
import org.example.projet_fil_rouge_ver1.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService service;

    public ExamController(ExamService service) { this.service = service; }

    @GetMapping
    public List<Exam> getAll() { return service.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exam create(@Valid @RequestBody ExamRequest req) { return service.create(req); }
}
