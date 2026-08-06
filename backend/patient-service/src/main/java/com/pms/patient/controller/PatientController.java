package com.pms.patient.controller;

import com.pms.patient.dto.*;
import com.pms.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // ── Patient CRUD ─────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/by-auth/{authUserId}")
    public ResponseEntity<PatientResponse> getPatientByAuthUserId(@PathVariable Long authUserId) {
        return ResponseEntity.ok(patientService.getPatientByAuthUserId(authUserId));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PatientResponse>> getActivePatients() {
        return ResponseEntity.ok(patientService.getActivePatients());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponse>> searchPatients(@RequestParam String name) {
        return ResponseEntity.ok(patientService.searchPatientsByName(name));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePatient(@PathVariable Long id) {
        patientService.deactivatePatient(id);
        return ResponseEntity.noContent().build();
    }

    // ── Medical Records ───────────────────────────────────────────────────────

    @PostMapping("/{patientId}/medical-records")
    public ResponseEntity<MedicalRecordResponse> addMedicalRecord(
            @PathVariable Long patientId,
            @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.addMedicalRecord(patientId, request));
    }

    @GetMapping("/{patientId}/medical-records")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatientMedicalHistory(patientId));
    }

    @GetMapping("/medical-records/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordResponse>> getDoctorRecords(@PathVariable Long doctorId) {
        return ResponseEntity.ok(patientService.getDoctorPatientRecords(doctorId));
    }
}
