package com.pms.doctor.controller;

import com.pms.doctor.dto.*;
import com.pms.doctor.entity.AppointmentStatus;
import com.pms.doctor.entity.PrescriptionStatus;
import com.pms.doctor.entity.Specialization;
import com.pms.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // ── Doctor CRUD ───────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id, @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/by-auth/{authUserId}")
    public ResponseEntity<DoctorResponse> getDoctorByAuthUserId(@PathVariable Long authUserId) {
        return ResponseEntity.ok(doctorService.getDoctorByAuthUserId(authUserId));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/available")
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors() {
        return ResponseEntity.ok(doctorService.getAvailableDoctors());
    }

    @GetMapping("/specialization/{spec}")
    public ResponseEntity<List<DoctorResponse>> getBySpecialization(@PathVariable Specialization spec) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(spec));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponse>> searchDoctors(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctors(name));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<DoctorResponse> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.toggleAvailability(id));
    }

    // ── Appointments ──────────────────────────────────────────────────────────

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> scheduleAppointment(
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.scheduleAppointment(request));
    }

    @PatchMapping("/appointments/{id}/status")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(doctorService.updateAppointmentStatus(id, status, notes));
    }

    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getDoctorAppointments(doctorId));
    }

    @GetMapping("/appointments/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorService.getPatientAppointments(patientId));
    }

    @GetMapping("/{doctorId}/appointments/status")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointmentsByStatus(
            @PathVariable Long doctorId, @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(doctorService.getDoctorAppointmentsByStatus(doctorId, status));
    }
}
