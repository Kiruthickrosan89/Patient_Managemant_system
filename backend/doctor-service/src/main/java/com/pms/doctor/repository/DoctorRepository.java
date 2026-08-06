package com.pms.doctor.repository;

import com.pms.doctor.entity.Doctor;
import com.pms.doctor.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByAuthUserId(Long authUserId);
    boolean existsByEmail(String email);
    List<Doctor> findBySpecialization(Specialization specialization);
    List<Doctor> findByIsAvailableTrue();

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> searchByName(@Param("name") String name);
}
