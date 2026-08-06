package com.pms.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * One drug line item in a dispense log.
 */
@Entity
@Table(name = "dispense_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DispenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispense_log_id", nullable = false)
    private DispenseLog dispenseLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @Column(nullable = false)
    private String medicineName;

    @Column(nullable = false)
    private Integer quantityDispensed;

    @Column(nullable = false)
    private Double unitPrice;

    /** quantityDispensed * unitPrice */
    @Column(nullable = false)
    private Double subtotal;
}
