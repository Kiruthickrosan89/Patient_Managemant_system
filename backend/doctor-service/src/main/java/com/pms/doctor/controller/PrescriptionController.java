package com.pms.doctor.controller;

import com.pms.doctor.dto.PrescriptionRequest;
import com.pms.doctor.dto.PrescriptionResponse;
import com.pms.doctor.entity.PrescriptionStatus;
import com.pms.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createPrescription(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getPrescription(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getPrescriptionById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PrescriptionResponse> updateStatus(
            @PathVariable Long id, @RequestParam PrescriptionStatus status) {
        return ResponseEntity.ok(doctorService.updatePrescriptionStatus(id, status));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionResponse>> getDoctorPrescriptions(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getDoctorPrescriptions(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponse>> getPatientPrescriptions(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorService.getPatientPrescriptions(patientId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PrescriptionResponse>> getByStatus(@PathVariable PrescriptionStatus status) {
        return ResponseEntity.ok(doctorService.getPrescriptionsByStatus(status));
    }
}
