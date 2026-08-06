package com.pms.doctor.repository;

import com.pms.doctor.entity.Prescription;
import com.pms.doctor.entity.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByDoctorIdOrderByIssuedAtDesc(Long doctorId);
    List<Prescription> findByPatientIdOrderByIssuedAtDesc(Long patientId);
    List<Prescription> findByStatus(PrescriptionStatus status);
}
