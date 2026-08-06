package com.pms.doctor.service;

import com.pms.doctor.dto.*;
import com.pms.doctor.entity.*;
import com.pms.doctor.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    // ── Doctor CRUD ───────────────────────────────────────────────────────────

    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Doctor doctor = Doctor.builder()
                .authUserId(request.getAuthUserId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .isAvailable(true)
                .build();

        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor created with ID: {}", saved.getId());
        return toDoctorResponse(saved);
    }

    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = findDoctorById(id);
        doctor.setFullName(request.getFullName());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setConsultationFee(request.getConsultationFee());
        return toDoctorResponse(doctorRepository.save(doctor));
    }

    public DoctorResponse getDoctorById(Long id) {
        return toDoctorResponse(findDoctorById(id));
    }

    public DoctorResponse getDoctorByAuthUserId(Long authUserId) {
        Doctor doctor = doctorRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found for auth user: " + authUserId));
        return toDoctorResponse(doctor);
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream().map(this::toDoctorResponse).collect(Collectors.toList());
    }

    public List<DoctorResponse> getAvailableDoctors() {
        return doctorRepository.findByIsAvailableTrue().stream().map(this::toDoctorResponse).collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsBySpecialization(Specialization specialization) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .map(this::toDoctorResponse).collect(Collectors.toList());
    }

    public List<DoctorResponse> searchDoctors(String name) {
        return doctorRepository.searchByName(name).stream().map(this::toDoctorResponse).collect(Collectors.toList());
    }

    @Transactional
    public DoctorResponse toggleAvailability(Long id) {
        Doctor doctor = findDoctorById(id);
        doctor.setIsAvailable(!doctor.getIsAvailable());
        return toDoctorResponse(doctorRepository.save(doctor));
    }

    // ── Appointments ──────────────────────────────────────────────────────────

    @Transactional
    public AppointmentResponse scheduleAppointment(AppointmentRequest request) {
        Doctor doctor = findDoctorById(request.getDoctorId());

        if (appointmentRepository.existsByDoctorIdAndScheduledAt(request.getDoctorId(), request.getScheduledAt())) {
            throw new IllegalArgumentException("Doctor already has an appointment at this time");
        }

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patientId(request.getPatientId())
                .patientName(request.getPatientName())
                .scheduledAt(request.getScheduledAt())
                .reasonForVisit(request.getReasonForVisit())
                .durationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 30)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment scheduled for doctor {} with patient {}", request.getDoctorId(), request.getPatientId());
        return toAppointmentResponse(saved);
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long id, AppointmentStatus status, String notes) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        appt.setStatus(status);
        if (notes != null && !notes.isBlank()) {
            appt.setConsultationNotes(notes);
        }
        return toAppointmentResponse(appointmentRepository.save(appt));
    }

    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByScheduledAtAsc(doctorId).stream()
                .map(this::toAppointmentResponse).collect(Collectors.toList());
    }

    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByScheduledAtAsc(patientId).stream()
                .map(this::toAppointmentResponse).collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointmentsByStatus(Long doctorId, AppointmentStatus status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status).stream()
                .map(this::toAppointmentResponse).collect(Collectors.toList());
    }

    // ── Prescriptions ─────────────────────────────────────────────────────────

    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        Doctor doctor = findDoctorById(request.getDoctorId());

        Prescription prescription = Prescription.builder()
                .doctor(doctor)
                .patientId(request.getPatientId())
                .patientName(request.getPatientName())
                .appointmentId(request.getAppointmentId())
                .diagnosis(request.getDiagnosis())
                .instructions(request.getInstructions())
                .status(PrescriptionStatus.ISSUED)
                .build();

        List<PrescriptionItem> items = request.getItems().stream()
                .map(itemReq -> PrescriptionItem.builder()
                        .prescription(prescription)
                        .medicineName(itemReq.getMedicineName())
                        .dosage(itemReq.getDosage())
                        .frequency(itemReq.getFrequency())
                        .duration(itemReq.getDuration())
                        .instructions(itemReq.getInstructions())
                        .build())
                .collect(Collectors.toList());
        prescription.setItems(items);

        Prescription saved = prescriptionRepository.save(prescription);
        log.info("Prescription created: ID={}", saved.getId());
        return toPrescriptionResponse(saved);
    }

    @Transactional
    public PrescriptionResponse updatePrescriptionStatus(Long id, PrescriptionStatus status) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));
        prescription.setStatus(status);
        return toPrescriptionResponse(prescriptionRepository.save(prescription));
    }

    public PrescriptionResponse getPrescriptionById(Long id) {
        Prescription p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));
        return toPrescriptionResponse(p);
    }

    public List<PrescriptionResponse> getDoctorPrescriptions(Long doctorId) {
        return prescriptionRepository.findByDoctorIdOrderByIssuedAtDesc(doctorId).stream()
                .map(this::toPrescriptionResponse).collect(Collectors.toList());
    }

    public List<PrescriptionResponse> getPatientPrescriptions(Long patientId) {
        return prescriptionRepository.findByPatientIdOrderByIssuedAtDesc(patientId).stream()
                .map(this::toPrescriptionResponse).collect(Collectors.toList());
    }

    public List<PrescriptionResponse> getPrescriptionsByStatus(PrescriptionStatus status) {
        return prescriptionRepository.findByStatus(status).stream()
                .map(this::toPrescriptionResponse).collect(Collectors.toList());
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private Doctor findDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + id));
    }

    private DoctorResponse toDoctorResponse(Doctor d) {
        return DoctorResponse.builder()
                .id(d.getId())
                .authUserId(d.getAuthUserId())
                .fullName(d.getFullName())
                .email(d.getEmail())
                .phone(d.getPhone())
                .specialization(d.getSpecialization())
                .qualification(d.getQualification())
                .experienceYears(d.getExperienceYears())
                .consultationFee(d.getConsultationFee())
                .isAvailable(d.getIsAvailable())
                .createdAt(d.getCreatedAt())
                .build();
    }

    private AppointmentResponse toAppointmentResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getFullName())
                .patientId(a.getPatientId())
                .patientName(a.getPatientName())
                .scheduledAt(a.getScheduledAt())
                .status(a.getStatus())
                .reasonForVisit(a.getReasonForVisit())
                .consultationNotes(a.getConsultationNotes())
                .durationMinutes(a.getDurationMinutes())
                .createdAt(a.getCreatedAt())
                .build();
    }

    private PrescriptionResponse toPrescriptionResponse(Prescription p) {
        List<PrescriptionItemResponse> items = p.getItems().stream()
                .map(i -> PrescriptionItemResponse.builder()
                        .id(i.getId())
                        .medicineName(i.getMedicineName())
                        .dosage(i.getDosage())
                        .frequency(i.getFrequency())
                        .duration(i.getDuration())
                        .instructions(i.getInstructions())
                        .build())
                .collect(Collectors.toList());

        return PrescriptionResponse.builder()
                .id(p.getId())
                .doctorId(p.getDoctor().getId())
                .doctorName(p.getDoctor().getFullName())
                .patientId(p.getPatientId())
                .patientName(p.getPatientName())
                .appointmentId(p.getAppointmentId())
                .status(p.getStatus())
                .diagnosis(p.getDiagnosis())
                .instructions(p.getInstructions())
                .issuedAt(p.getIssuedAt())
                .items(items)
                .build();
    }
}
