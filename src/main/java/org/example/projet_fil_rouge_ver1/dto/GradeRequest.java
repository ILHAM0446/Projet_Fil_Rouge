package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class GradeRequest {
    @NotNull public Long examId;
    @NotNull public Long studentId;
    @NotNull @DecimalMin("0.0") @DecimalMax("20.0") public Double value;
}
