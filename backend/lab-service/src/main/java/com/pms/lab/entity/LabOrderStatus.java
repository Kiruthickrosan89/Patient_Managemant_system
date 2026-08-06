package com.pms.lab.entity;

public enum LabOrderStatus {
    PENDING,       // Order raised, not yet assigned
    IN_PROGRESS,   // Lab tech has started processing
    COMPLETED,     // Results uploaded
    CANCELLED
}
