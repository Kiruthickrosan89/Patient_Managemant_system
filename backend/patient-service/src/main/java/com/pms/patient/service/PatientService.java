package com.pms.patient.service;

import com.pms.patient.dto.*;
import com.pms.patient.entity.*;
import com.pms.patient.repository.MedicalRecordRepository;
import com.pms.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Patient patient = Patient.builder()
                .authUserId(request.getAuthUserId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup() != null ? request.getBloodGroup() : BloodGroup.UNKNOWN)
                .address(request.getAddress())
                .allergies(request.getAllergies())
                .chronicConditions(request.getChronicConditions())
                .isActive(true)
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("Patient created with ID: {}", saved.getId());

        return toPatientResponse(saved);
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        patient.setFullName(request.getFullName());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAddress(request.getAddress());
        patient.setAllergies(request.getAllergies());
        patient.setChronicConditions(request.getChronicConditions());

        Patient updated = patientRepository.save(patient);
        log.info("Patient updated: ID={}", id);

        return toPatientResponse(updated);
    }

    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        return toPatientResponse(patient);
    }

    public PatientResponse getPatientByAuthUserId(Long authUserId) {
        Patient patient = patientRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found for user ID: " + authUserId));
        return toPatientResponse(patient);
    }

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::toPatientResponse)
                .collect(Collectors.toList());
    }

    public List<PatientResponse> getActivePatients() {
        return patientRepository.findByIsActiveTrue().stream()
                .map(this::toPatientResponse)
                .collect(Collectors.toList());
    }

    public List<PatientResponse> searchPatientsByName(String name) {
        return patientRepository.searchByName(name).stream()
                .map(this::toPatientResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivatePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patient.setIsActive(false);
        patientRepository.save(patient);
        log.info("Patient deactivated: ID={}", id);
    }

    @Transactional
    public MedicalRecordResponse addMedicalRecord(Long patientId, MedicalRecordRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctorId(request.getDoctorId())
                .doctorName(request.getDoctorName())
                .diagnosis(request.getDiagnosis())
                .symptoms(request.getSymptoms())
                .treatment(request.getTreatment())
                .notes(request.getNotes())
                .build();

        MedicalRecord saved = medicalRecordRepository.save(record);
        log.info("Medical record added for patient ID: {}", patientId);

        return toMedicalRecordResponse(saved);
    }

    public List<MedicalRecordResponse> getPatientMedicalHistory(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(patientId).stream()
                .map(this::toMedicalRecordResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponse> getDoctorPatientRecords(Long doctorId) {
        return medicalRecordRepository.findByDoctorIdOrderByVisitDateDesc(doctorId).stream()
                .map(this::toMedicalRecordResponse)
                .collect(Collectors.toList());
    }

    private PatientResponse toPatientResponse(Patient patient) {
        int age = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();

        return PatientResponse.builder()
                .id(patient.getId())
                .authUserId(patient.getAuthUserId())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .age(age)
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .allergies(patient.getAllergies())
                .chronicConditions(patient.getChronicConditions())
                .isActive(patient.getIsActive())
                .createdAt(patient.getCreatedAt())
                .build();
    }

    private MedicalRecordResponse toMedicalRecordResponse(MedicalRecord record) {
        return MedicalRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatient().getId())
                .patientName(record.getPatient().getFullName())
                .doctorId(record.getDoctorId())
                .doctorName(record.getDoctorName())
                .diagnosis(record.getDiagnosis())
                .symptoms(record.getSymptoms())
                .treatment(record.getTreatment())
                .notes(record.getNotes())
                .visitDate(record.getVisitDate())
                .build();
    }
}
