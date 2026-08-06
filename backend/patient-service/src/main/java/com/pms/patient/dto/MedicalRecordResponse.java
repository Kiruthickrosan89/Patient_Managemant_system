package com.pms.patient.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MedicalRecordResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private String notes;
    private LocalDateTime visitDate;
}
