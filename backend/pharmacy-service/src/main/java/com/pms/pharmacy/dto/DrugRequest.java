package com.pms.pharmacy.dto;

import com.pms.pharmacy.entity.DrugCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DrugRequest {

    @NotBlank(message = "Drug name is required")
    private String name;

    private String genericName;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotNull(message = "Category is required")
    private DrugCategory category;

    @NotBlank(message = "Strength is required")
    private String strength;

    @NotBlank(message = "Dosage form is required")
    private String dosageForm;

    @NotNull @Min(0)
    private Integer quantityInStock;

    @Min(0)
    private Integer reorderLevel;

    @NotNull @DecimalMin("0.0")
    private Double unitPrice;

    @NotNull @Future
    private LocalDate expiryDate;

    private String batchNumber;
}
