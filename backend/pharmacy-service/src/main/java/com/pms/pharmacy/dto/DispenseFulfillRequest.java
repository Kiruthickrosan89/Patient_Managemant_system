package com.pms.pharmacy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Request to fulfill a prescription — submitted by a pharmacist.
 */
@Data
public class DispenseFulfillRequest {

    @NotNull(message = "Prescription ID is required")
    private Long prescriptionId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "Doctor name is required")
    private String doctorName;

    @NotBlank(message = "Dispensed by is required")
    private String dispensedBy;

    @NotEmpty(message = "At least one dispense item is required")
    @Valid
    private List<DispenseItemRequest> items;

    private String notes;
}
