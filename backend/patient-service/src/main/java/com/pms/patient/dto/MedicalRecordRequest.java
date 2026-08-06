package com.pms.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalRecordRequest {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "Doctor name is required")
    private String doctorName;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    private String symptoms;

    private String treatment;

    private String notes;
}
