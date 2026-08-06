package com.pms.doctor.dto;

import com.pms.doctor.entity.PrescriptionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class PrescriptionResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private Long appointmentId;
    private PrescriptionStatus status;
    private String diagnosis;
    private String instructions;
    private LocalDateTime issuedAt;
    private List<PrescriptionItemResponse> items;
}
