package com.pms.pharmacy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DispenseItemRequest {

    @NotNull(message = "Drug ID is required")
    private Long drugId;

    @NotNull @Min(1)
    private Integer quantityDispensed;
}
