package com.pms.lab.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

/**
 * Payload submitted by a lab technician when uploading results.
 */
@Data
public class LabResultRequest {

    @NotEmpty(message = "Diagnostic payload must not be empty")
    private Map<String, Object> diagnosticPayload;

    @NotBlank(message = "Processed by field is required")
    private String processedBy;

    private String remarks;
}
