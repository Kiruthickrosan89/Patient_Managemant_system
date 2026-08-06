package com.pms.pharmacy.entity;

public enum DispenseStatus {
    PENDING,         // Prescription received, not yet processed
    PARTIALLY_FILLED, // Some items dispensed, others out of stock
    DISPENSED,       // All items dispensed
    CANCELLED
}
