package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.NotNull;
import org.example.projet_fil_rouge_ver1.model.Absence;
import java.time.LocalDate;

public class AbsenceRequest {
    @NotNull public Long studentId;
    @NotNull public Long courseId;
    @NotNull public LocalDate date;
    public Absence.Justified justified = Absence.Justified.NON;
}
