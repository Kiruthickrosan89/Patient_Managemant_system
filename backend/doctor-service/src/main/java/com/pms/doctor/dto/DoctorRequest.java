package com.pms.doctor.dto;

import com.pms.doctor.entity.Specialization;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DoctorRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Specialization is required")
    private Specialization specialization;

    private String qualification;

    @Min(0) @Max(60)
    private Integer experienceYears;

    @DecimalMin("0.0")
    private Double consultationFee;

    private Long authUserId;
}
