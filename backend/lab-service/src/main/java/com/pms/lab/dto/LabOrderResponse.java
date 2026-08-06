package com.pms.lab.dto;

import com.pms.lab.entity.LabOrderStatus;
import com.pms.lab.entity.LabType;
import com.pms.lab.entity.Priority;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data @Builder
public class LabOrderResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private LabType labType;
    private String testName;
    private Priority priority;
    private LabOrderStatus status;
    private Map<String, Object> diagnosticPayload;
    private String processedBy;
    private String remarks;
    private LocalDateTime orderedAt;
    private LocalDateTime completedAt;
}
