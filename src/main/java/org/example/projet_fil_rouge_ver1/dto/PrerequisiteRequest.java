package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.NotNull;

public class PrerequisiteRequest {
    @NotNull public Long courseId;
    @NotNull public Long prerequisiteId;
    public boolean equivalenceAllowed = false;
}
