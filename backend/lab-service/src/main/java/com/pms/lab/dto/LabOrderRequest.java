package com.pms.lab.dto;

import com.pms.lab.entity.LabType;
import com.pms.lab.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class LabOrderRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "Doctor name is required")
    private String doctorName;

    @NotNull(message = "Lab type is required")
    private LabType labType;

    @NotBlank(message = "Test name is required")
    private String testName;

    private Priority priority;

    /** Optional initial diagnostic payload; can be populated later when results are ready */
    private Map<String, Object> diagnosticPayload;

    private String remarks;
}
