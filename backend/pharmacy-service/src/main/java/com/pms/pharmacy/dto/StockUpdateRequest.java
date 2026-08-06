package com.pms.pharmacy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateRequest {

    @NotNull @Min(1)
    private Integer quantity;

    /** true = add stock (restock), false = reduce stock (manual adjustment) */
    private boolean addStock = true;

    private String reason;
}
