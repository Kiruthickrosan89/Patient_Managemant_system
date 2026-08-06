package com.pms.pharmacy.dto;

import com.pms.pharmacy.entity.BillingStatus;
import com.pms.pharmacy.entity.DispenseStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class DispenseLogResponse {
    private Long id;
    private Long prescriptionId;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private DispenseStatus status;
    private BillingStatus billingStatus;
    private Double totalAmount;
    private String dispensedBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime dispensedAt;
    private List<DispenseItemResponse> items;
}
