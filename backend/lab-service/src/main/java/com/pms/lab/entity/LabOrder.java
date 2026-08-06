package com.pms.lab.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * A lab test order.
 *
 * The {@code diagnosticPayload} column is stored as PostgreSQL JSONB.
 * Its structure varies per lab type:
 *
 *   XRAY   → { "bodyPart": "Chest", "view": "PA", "findings": "...", "impression": "..." }
 *   BLOOD  → { "CBC": {...}, "LFT": {...}, "RFT": {...}, ... }
 *   SUGAR  → { "fasting": 98, "postPrandial": 145, "HbA1c": 5.9, "unit": "mg/dL" }
 */
@Entity
@Table(name = "lab_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String patientName;

    /** Doctor who ordered the test */
    @Column(nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private String doctorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private LabType labType;

    /** Specific test name e.g. "Complete Blood Count", "Fasting Glucose", "Chest X-Ray" */
    @Column(nullable = false)
    private String testName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Priority priority = Priority.ROUTINE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private LabOrderStatus status = LabOrderStatus.PENDING;

    /** Flexible JSONB column: structure depends on labType */
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> diagnosticPayload;

    /** Lab technician who processed the order */
    private String processedBy;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedAt;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.orderedAt = LocalDateTime.now();
    }
}
