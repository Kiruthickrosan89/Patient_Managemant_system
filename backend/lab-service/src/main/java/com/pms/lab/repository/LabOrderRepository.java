package com.pms.lab.repository;

import com.pms.lab.entity.LabOrder;
import com.pms.lab.entity.LabOrderStatus;
import com.pms.lab.entity.LabType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {

    List<LabOrder> findByPatientIdOrderByOrderedAtDesc(Long patientId);
    List<LabOrder> findByDoctorIdOrderByOrderedAtDesc(Long doctorId);
    List<LabOrder> findByLabTypeOrderByOrderedAtDesc(LabType labType);
    List<LabOrder> findByStatusOrderByOrderedAtAsc(LabOrderStatus status);
    List<LabOrder> findByLabTypeAndStatusOrderByOrderedAtAsc(LabType labType, LabOrderStatus status);

    @Query("SELECT lo FROM LabOrder lo WHERE lo.labType = :labType AND lo.status = 'PENDING' ORDER BY lo.orderedAt ASC")
    List<LabOrder> findPendingByLabType(@Param("labType") LabType labType);

    long countByStatus(LabOrderStatus status);
    long countByLabType(LabType labType);
}
