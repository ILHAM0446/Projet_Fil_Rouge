package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class GradingFormulaRequest {
    @NotNull public Long courseId;
    @DecimalMin("0.0") @DecimalMax("100.0") public double weightCC = 30.0;
    @DecimalMin("0.0") @DecimalMax("100.0") public double weightExam = 50.0;
    @DecimalMin("0.0") @DecimalMax("100.0") public double weightProjet = 20.0;
}
