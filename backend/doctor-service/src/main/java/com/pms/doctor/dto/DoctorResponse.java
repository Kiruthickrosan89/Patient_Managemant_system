package com.pms.doctor.dto;

import com.pms.doctor.entity.Specialization;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class DoctorResponse {
    private Long id;
    private Long authUserId;
    private String fullName;
    private String email;
    private String phone;
    private Specialization specialization;
    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
}
