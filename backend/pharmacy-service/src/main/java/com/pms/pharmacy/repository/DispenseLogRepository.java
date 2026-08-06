package com.pms.pharmacy.repository;

import com.pms.pharmacy.entity.BillingStatus;
import com.pms.pharmacy.entity.DispenseLog;
import com.pms.pharmacy.entity.DispenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispenseLogRepository extends JpaRepository<DispenseLog, Long> {

    Optional<DispenseLog> findByPrescriptionId(Long prescriptionId);

    boolean existsByPrescriptionId(Long prescriptionId);

    List<DispenseLog> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<DispenseLog> findByStatus(DispenseStatus status);

    List<DispenseLog> findByBillingStatus(BillingStatus billingStatus);

    long countByStatus(DispenseStatus status);
}
