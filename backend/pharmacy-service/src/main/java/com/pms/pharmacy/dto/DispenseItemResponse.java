package com.pms.pharmacy.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DispenseItemResponse {
    private Long id;
    private Long drugId;
    private String medicineName;
    private Integer quantityDispensed;
    private Double unitPrice;
    private Double subtotal;
}
