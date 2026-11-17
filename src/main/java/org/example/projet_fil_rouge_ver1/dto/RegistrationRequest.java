package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.NotNull;

public class RegistrationRequest {
    @NotNull public Long studentId;
    @NotNull public Long courseId;
}
