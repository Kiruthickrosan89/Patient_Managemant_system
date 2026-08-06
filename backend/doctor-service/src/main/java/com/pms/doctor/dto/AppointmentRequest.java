package com.pms.doctor.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotNull(message = "Scheduled time is required")
    @Future(message = "Appointment must be in the future")
    private LocalDateTime scheduledAt;

    private String reasonForVisit;

    private Integer durationMinutes;
}
