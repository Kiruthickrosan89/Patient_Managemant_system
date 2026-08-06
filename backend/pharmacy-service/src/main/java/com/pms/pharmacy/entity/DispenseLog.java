package com.pms.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the dispensing of a prescription.
 * One DispenseLog per prescription fulfillment.
 */
@Entity
@Table(name = "dispense_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DispenseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Prescription ID from doctor-service */
    @Column(nullable = false, unique = true)
    private Long prescriptionId;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private String doctorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DispenseStatus status = DispenseStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BillingStatus billingStatus = BillingStatus.UNPAID;

    /** Total amount billed */
    @Column(nullable = false)
    @Builder.Default
    private Double totalAmount = 0.0;

    /** Pharmacist who dispensed */
    private String dispensedBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime dispensedAt;

    @OneToMany(mappedBy = "dispenseLog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DispenseItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
