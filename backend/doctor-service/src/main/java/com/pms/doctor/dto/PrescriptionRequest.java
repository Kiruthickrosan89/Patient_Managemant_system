package com.pms.doctor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    private Long appointmentId;

    private String diagnosis;

    private String instructions;

    @NotEmpty(message = "At least one prescription item is required")
    @Valid
    private List<PrescriptionItemRequest> items;
}
