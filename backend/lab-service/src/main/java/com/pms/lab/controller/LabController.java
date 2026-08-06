package com.pms.lab.controller;

import com.pms.lab.dto.*;
import com.pms.lab.entity.LabOrderStatus;
import com.pms.lab.entity.LabType;
import com.pms.lab.service.LabService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lab")
@RequiredArgsConstructor
public class LabController {

    private final LabService labService;

    // ── Order Management ──────────────────────────────────────────────────────

    @PostMapping("/orders")
    public ResponseEntity<LabOrderResponse> createOrder(@Valid @RequestBody LabOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labService.createOrder(request));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<LabOrderResponse>> getAllOrders() {
        return ResponseEntity.ok(labService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<LabOrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(labService.getOrderById(id));
    }

    @GetMapping("/orders/patient/{patientId}")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(labService.getOrdersByPatient(patientId));
    }

    @GetMapping("/orders/doctor/{doctorId}")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(labService.getOrdersByDoctor(doctorId));
    }

    @GetMapping("/orders/type/{labType}")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByType(@PathVariable LabType labType) {
        return ResponseEntity.ok(labService.getOrdersByLabType(labType));
    }

    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByStatus(@PathVariable LabOrderStatus status) {
        return ResponseEntity.ok(labService.getOrdersByStatus(status));
    }

    @GetMapping("/orders/type/{labType}/pending")
    public ResponseEntity<List<LabOrderResponse>> getPendingByType(@PathVariable LabType labType) {
        return ResponseEntity.ok(labService.getPendingOrdersByLabType(labType));
    }

    @GetMapping("/orders/type/{labType}/status/{status}")
    public ResponseEntity<List<LabOrderResponse>> getByTypeAndStatus(
            @PathVariable LabType labType, @PathVariable LabOrderStatus status) {
        return ResponseEntity.ok(labService.getOrdersByLabTypeAndStatus(labType, status));
    }

    // ── Lab Workflows ─────────────────────────────────────────────────────────

    /** Lab technician claims an order (PENDING → IN_PROGRESS) */
    @PatchMapping("/orders/{id}/assign")
    public ResponseEntity<LabOrderResponse> assignTechnician(
            @PathVariable Long id, @RequestParam String technicianName) {
        return ResponseEntity.ok(labService.assignLabTechnician(id, technicianName));
    }

    /** Upload diagnostic results (IN_PROGRESS → COMPLETED with JSONB payload) */
    @PutMapping("/orders/{id}/results")
    public ResponseEntity<LabOrderResponse> uploadResults(
            @PathVariable Long id, @Valid @RequestBody LabResultRequest request) {
        return ResponseEntity.ok(labService.uploadResults(id, request));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<LabOrderResponse> updateStatus(
            @PathVariable Long id, @RequestParam LabOrderStatus status) {
        return ResponseEntity.ok(labService.updateOrderStatus(id, status));
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        labService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = Map.of(
                "pending",     labService.countByStatus(LabOrderStatus.PENDING),
                "inProgress",  labService.countByStatus(LabOrderStatus.IN_PROGRESS),
                "completed",   labService.countByStatus(LabOrderStatus.COMPLETED),
                "xray",        labService.countByLabType(LabType.XRAY),
                "blood",       labService.countByLabType(LabType.BLOOD),
                "sugar",       labService.countByLabType(LabType.SUGAR)
        );
        return ResponseEntity.ok(stats);
    }
}
