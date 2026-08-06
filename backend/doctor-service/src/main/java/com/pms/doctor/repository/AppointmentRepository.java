package com.pms.doctor.repository;

import com.pms.doctor.entity.Appointment;
import com.pms.doctor.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorIdOrderByScheduledAtAsc(Long doctorId);
    List<Appointment> findByPatientIdOrderByScheduledAtAsc(Long patientId);
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);
    List<Appointment> findByDoctorIdAndScheduledAtBetween(Long doctorId, LocalDateTime from, LocalDateTime to);

    boolean existsByDoctorIdAndScheduledAt(Long doctorId, LocalDateTime scheduledAt);
}
