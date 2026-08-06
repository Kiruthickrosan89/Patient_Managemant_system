package com.pms.patient.repository;

import com.pms.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByAuthUserId(Long authUserId);

    boolean existsByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Patient> searchByName(@Param("name") String name);

    List<Patient> findByIsActiveTrue();
}
