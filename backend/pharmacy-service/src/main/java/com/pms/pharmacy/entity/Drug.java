package com.pms.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a drug in the pharmacy inventory.
 */
@Entity
@Table(name = "drugs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /** Generic/chemical name */
    private String genericName;

    @Column(nullable = false)
    private String manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrugCategory category;

    /** e.g. "500mg", "10mg/5ml" */
    @Column(nullable = false)
    private String strength;

    /** e.g. "Tablet", "Capsule", "Syrup", "Injection" */
    @Column(nullable = false)
    private String dosageForm;

    @Column(nullable = false)
    private Integer quantityInStock;

    /** Minimum stock threshold before reorder alert */
    @Column(nullable = false)
    @Builder.Default
    private Integer reorderLevel = 10;

    /** Price per unit in INR */
    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private LocalDate expiryDate;

    /** Batch/lot number */
    private String batchNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isLowStock() {
        return this.quantityInStock <= this.reorderLevel;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.expiryDate);
    }
}
