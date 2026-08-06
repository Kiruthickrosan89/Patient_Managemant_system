package com.pms.pharmacy.dto;

import com.pms.pharmacy.entity.DrugCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class DrugResponse {
    private Long id;
    private String name;
    private String genericName;
    private String manufacturer;
    private DrugCategory category;
    private String strength;
    private String dosageForm;
    private Integer quantityInStock;
    private Integer reorderLevel;
    private Double unitPrice;
    private LocalDate expiryDate;
    private String batchNumber;
    private Boolean lowStock;
    private Boolean expired;
    private LocalDateTime createdAt;
}
