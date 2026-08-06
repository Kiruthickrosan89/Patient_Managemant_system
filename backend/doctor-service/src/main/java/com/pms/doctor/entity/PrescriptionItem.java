package com.pms.doctor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescription_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @Column(nullable = false)
    private String medicineName;

    @Column(nullable = false)
    private String dosage;          // e.g. "500mg"

    @Column(nullable = false)
    private String frequency;       // e.g. "Twice daily"

    @Column(nullable = false)
    private String duration;        // e.g. "7 days"

    private String instructions;    // e.g. "After food"
}
