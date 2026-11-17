package org.example.projet_fil_rouge_ver1.dto;

import jakarta.validation.constraints.NotNull;
import org.example.projet_fil_rouge_ver1.model.Attendance;
import java.time.LocalDate;

public class AttendanceRequest {
    @NotNull public Long studentId;
    @NotNull public Long courseId;
    @NotNull public LocalDate sessionDate;
    public Attendance.AttendanceStatus status = Attendance.AttendanceStatus.PRESENT;
}
