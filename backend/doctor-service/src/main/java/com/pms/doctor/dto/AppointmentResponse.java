package com.pms.doctor.dto;

import com.pms.doctor.entity.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class AppointmentResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
    private String reasonForVisit;
    private String consultationNotes;
    private Integer durationMinutes;
    private LocalDateTime createdAt;
}
