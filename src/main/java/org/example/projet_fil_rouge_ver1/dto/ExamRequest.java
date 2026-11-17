package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.NotNull;
import org.example.projet_fil_rouge_ver1.model.Exam;
import java.time.LocalDate;

public class ExamRequest {
    @NotNull public Long courseId;
    @NotNull public LocalDate date;
    public Exam.EvaluationType evaluationType = Exam.EvaluationType.EXAM;
}
