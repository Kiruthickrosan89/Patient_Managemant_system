package com.pms.patient.dto;

import com.pms.patient.entity.BloodGroup;
import com.pms.patient.entity.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PatientResponse {
    private Long id;
    private Long authUserId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private int age;
    private Gender gender;
    private BloodGroup bloodGroup;
    private String address;
    private String allergies;
    private String chronicConditions;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
