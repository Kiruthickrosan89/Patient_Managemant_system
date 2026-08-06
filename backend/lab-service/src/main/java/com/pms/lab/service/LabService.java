package com.pms.lab.service;

import com.pms.lab.dto.*;
import com.pms.lab.entity.*;
import com.pms.lab.repository.LabOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabService {

    private final LabOrderRepository labOrderRepository;

    // ── Lab Order CRUD ────────────────────────────────────────────────────────

    @Transactional
    public LabOrderResponse createOrder(LabOrderRequest request) {
        LabOrder order = LabOrder.builder()
                .patientId(request.getPatientId())
                .patientName(request.getPatientName())
                .doctorId(request.getDoctorId())
                .doctorName(request.getDoctorName())
                .labType(request.getLabType())
                .testName(request.getTestName())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.ROUTINE)
                .status(LabOrderStatus.PENDING)
                .diagnosticPayload(request.getDiagnosticPayload())
                .remarks(request.getRemarks())
                .build();

        LabOrder saved = labOrderRepository.save(order);
        log.info("Lab order created: ID={}, Type={}, Patient={}", saved.getId(), saved.getLabType(), saved.getPatientId());
        return toResponse(saved);
    }

    @Transactional
    public LabOrderResponse updateOrderStatus(Long id, LabOrderStatus newStatus) {
        LabOrder order = findOrderById(id);
        order.setStatus(newStatus);

        if (newStatus == LabOrderStatus.COMPLETED && order.getCompletedAt() == null) {
            order.setCompletedAt(LocalDateTime.now());
        }

        LabOrder updated = labOrderRepository.save(order);
        log.info("Lab order {} status updated to {}", id, newStatus);
        return toResponse(updated);
    }

    @Transactional
    public LabOrderResponse uploadResults(Long orderId, LabResultRequest request) {
        LabOrder order = findOrderById(orderId);

        if (order.getStatus() == LabOrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Results already uploaded for order: " + orderId);
        }

        order.setDiagnosticPayload(request.getDiagnosticPayload());
        order.setProcessedBy(request.getProcessedBy());
        order.setRemarks(request.getRemarks());
        order.setStatus(LabOrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());

        LabOrder saved = labOrderRepository.save(order);
        log.info("Lab results uploaded for order {}", orderId);
        return toResponse(saved);
    }

    @Transactional
    public LabOrderResponse assignLabTechnician(Long orderId, String technicianName) {
        LabOrder order = findOrderById(orderId);

        if (order.getStatus() != LabOrderStatus.PENDING) {
            throw new IllegalArgumentException("Can only assign technician to PENDING orders");
        }

        order.setProcessedBy(technicianName);
        order.setStatus(LabOrderStatus.IN_PROGRESS);
        LabOrder updated = labOrderRepository.save(order);
        log.info("Lab order {} assigned to technician: {}", orderId, technicianName);
        return toResponse(updated);
    }

    public LabOrderResponse getOrderById(Long id) {
        return toResponse(findOrderById(id));
    }

    public List<LabOrderResponse> getAllOrders() {
        return labOrderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<LabOrderResponse> getOrdersByPatient(Long patientId) {
        return labOrderRepository.findByPatientIdOrderByOrderedAtDesc(patientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<LabOrderResponse> getOrdersByDoctor(Long doctorId) {
        return labOrderRepository.findByDoctorIdOrderByOrderedAtDesc(doctorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<LabOrderResponse> getOrdersByLabType(LabType labType) {
        return labOrderRepository.findByLabTypeOrderByOrderedAtDesc(labType).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<LabOrderResponse> getOrdersByStatus(LabOrderStatus status) {
        return labOrderRepository.findByStatusOrderByOrderedAtAsc(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<LabOrderResponse> getPendingOrdersByLabType(LabType labType) {
        return labOrderRepository.findPendingByLabType(labType).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<LabOrderResponse> getOrdersByLabTypeAndStatus(LabType labType, LabOrderStatus status) {
        return labOrderRepository.findByLabTypeAndStatusOrderByOrderedAtAsc(labType, status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long id) {
        LabOrder order = findOrderById(id);

        if (order.getStatus() == LabOrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot cancel completed order");
        }

        order.setStatus(LabOrderStatus.CANCELLED);
        labOrderRepository.save(order);
        log.info("Lab order {} cancelled", id);
    }

    // ── Statistics ────────────────────────────────────────────────────────────

    public long countByStatus(LabOrderStatus status) {
        return labOrderRepository.countByStatus(status);
    }

    public long countByLabType(LabType labType) {
        return labOrderRepository.countByLabType(labType);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private LabOrder findOrderById(Long id) {
        return labOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lab order not found: " + id));
    }

    private LabOrderResponse toResponse(LabOrder order) {
        return LabOrderResponse.builder()
                .id(order.getId())
                .patientId(order.getPatientId())
                .patientName(order.getPatientName())
                .doctorId(order.getDoctorId())
                .doctorName(order.getDoctorName())
                .labType(order.getLabType())
                .testName(order.getTestName())
                .priority(order.getPriority())
                .status(order.getStatus())
                .diagnosticPayload(order.getDiagnosticPayload())
                .processedBy(order.getProcessedBy())
                .remarks(order.getRemarks())
                .orderedAt(order.getOrderedAt())
                .completedAt(order.getCompletedAt())
                .build();
    }
}
