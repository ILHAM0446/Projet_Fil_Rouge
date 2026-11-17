package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ExamRequest {
    @NotNull public Long courseId;
    @NotNull public LocalDate date;
}
