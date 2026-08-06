package com.pms.pharmacy.controller;

import com.pms.pharmacy.dto.DispenseFulfillRequest;
import com.pms.pharmacy.dto.DispenseLogResponse;
import com.pms.pharmacy.entity.BillingStatus;
import com.pms.pharmacy.entity.DispenseStatus;
import com.pms.pharmacy.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    // ── Prescription Fulfillment ──────────────────────────────────────────────

    @PostMapping("/dispense")
    public ResponseEntity<DispenseLogResponse> fulfillPrescription(
            @Valid @RequestBody DispenseFulfillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.fulfillPrescription(request));
    }

    @GetMapping("/dispense/{id}")
    public ResponseEntity<DispenseLogResponse> getDispenseLog(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getDispenseLogById(id));
    }

    @GetMapping("/dispense/prescription/{prescriptionId}")
    public ResponseEntity<DispenseLogResponse> getByPrescription(@PathVariable Long prescriptionId) {
        return ResponseEntity.ok(pharmacyService.getDispenseLogByPrescription(prescriptionId));
    }

    @GetMapping("/dispense")
    public ResponseEntity<List<DispenseLogResponse>> getAllDispenseLogs() {
        return ResponseEntity.ok(pharmacyService.getAllDispenseLogs());
    }

    @GetMapping("/dispense/patient/{patientId}")
    public ResponseEntity<List<DispenseLogResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(pharmacyService.getDispenseLogsByPatient(patientId));
    }

    @GetMapping("/dispense/status/{status}")
    public ResponseEntity<List<DispenseLogResponse>> getByStatus(@PathVariable DispenseStatus status) {
        return ResponseEntity.ok(pharmacyService.getDispenseLogsByStatus(status));
    }

    @GetMapping("/dispense/billing/{billingStatus}")
    public ResponseEntity<List<DispenseLogResponse>> getByBillingStatus(@PathVariable BillingStatus billingStatus) {
        return ResponseEntity.ok(pharmacyService.getDispenseLogsByBillingStatus(billingStatus));
    }

    @PatchMapping("/dispense/{id}/billing")
    public ResponseEntity<DispenseLogResponse> updateBilling(
            @PathVariable Long id, @RequestParam BillingStatus billingStatus) {
        return ResponseEntity.ok(pharmacyService.updateBillingStatus(id, billingStatus));
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(Map.of(
                "lowStockDrugs",    pharmacyService.countLowStockDrugs(),
                "pendingDispense",  pharmacyService.countPendingDispense(),
                "dispensedTotal",   pharmacyService.countDispensedToday()
        ));
    }
}
