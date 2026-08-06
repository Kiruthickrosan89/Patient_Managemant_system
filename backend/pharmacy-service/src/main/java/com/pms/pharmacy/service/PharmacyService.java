package com.pms.pharmacy.service;

import com.pms.pharmacy.dto.*;
import com.pms.pharmacy.entity.*;
import com.pms.pharmacy.repository.DispenseLogRepository;
import com.pms.pharmacy.repository.DrugRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacyService {

    private final DrugRepository drugRepository;
    private final DispenseLogRepository dispenseLogRepository;

    // ── Drug Inventory ────────────────────────────────────────────────────────

    @Transactional
    public DrugResponse addDrug(DrugRequest request) {
        if (drugRepository.existsByNameAndStrengthAndDosageForm(
                request.getName(), request.getStrength(), request.getDosageForm())) {
            throw new IllegalArgumentException("Drug already exists: " + request.getName()
                    + " " + request.getStrength() + " " + request.getDosageForm());
        }

        Drug drug = Drug.builder()
                .name(request.getName())
                .genericName(request.getGenericName())
                .manufacturer(request.getManufacturer())
                .category(request.getCategory())
                .strength(request.getStrength())
                .dosageForm(request.getDosageForm())
                .quantityInStock(request.getQuantityInStock())
                .reorderLevel(request.getReorderLevel() != null ? request.getReorderLevel() : 10)
                .unitPrice(request.getUnitPrice())
                .expiryDate(request.getExpiryDate())
                .batchNumber(request.getBatchNumber())
                .build();

        Drug saved = drugRepository.save(drug);
        log.info("Drug added to inventory: {} (ID={})", saved.getName(), saved.getId());
        return toDrugResponse(saved);
    }

    @Transactional
    public DrugResponse updateDrug(Long id, DrugRequest request) {
        Drug drug = findDrugById(id);
        drug.setName(request.getName());
        drug.setGenericName(request.getGenericName());
        drug.setManufacturer(request.getManufacturer());
        drug.setCategory(request.getCategory());
        drug.setStrength(request.getStrength());
        drug.setDosageForm(request.getDosageForm());
        drug.setUnitPrice(request.getUnitPrice());
        drug.setExpiryDate(request.getExpiryDate());
        drug.setBatchNumber(request.getBatchNumber());
        if (request.getReorderLevel() != null) {
            drug.setReorderLevel(request.getReorderLevel());
        }
        return toDrugResponse(drugRepository.save(drug));
    }

    @Transactional
    public DrugResponse updateStock(Long id, StockUpdateRequest request) {
        Drug drug = findDrugById(id);
        if (request.isAddStock()) {
            drug.setQuantityInStock(drug.getQuantityInStock() + request.getQuantity());
            log.info("Restocked drug {}: +{} units", drug.getName(), request.getQuantity());
        } else {
            int newQty = drug.getQuantityInStock() - request.getQuantity();
            if (newQty < 0) {
                throw new IllegalArgumentException("Insufficient stock for drug: " + drug.getName());
            }
            drug.setQuantityInStock(newQty);
        }
        return toDrugResponse(drugRepository.save(drug));
    }

    public DrugResponse getDrugById(Long id) {
        return toDrugResponse(findDrugById(id));
    }

    public List<DrugResponse> getAllDrugs() {
        return drugRepository.findAll().stream().map(this::toDrugResponse).collect(Collectors.toList());
    }

    public List<DrugResponse> getDrugsByCategory(DrugCategory category) {
        return drugRepository.findByCategory(category).stream().map(this::toDrugResponse).collect(Collectors.toList());
    }

    public List<DrugResponse> searchDrugs(String name) {
        return drugRepository.searchByName(name).stream().map(this::toDrugResponse).collect(Collectors.toList());
    }

    public List<DrugResponse> getLowStockDrugs() {
        return drugRepository.findLowStockDrugs().stream().map(this::toDrugResponse).collect(Collectors.toList());
    }

    public List<DrugResponse> getExpiredOrExpiringDrugs(int daysAhead) {
        LocalDate threshold = LocalDate.now().plusDays(daysAhead);
        return drugRepository.findByExpiryDateLessThanEqual(threshold).stream()
                .map(this::toDrugResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteDrug(Long id) {
        Drug drug = findDrugById(id);
        drugRepository.delete(drug);
        log.info("Drug removed from inventory: ID={}", id);
    }

    // ── Prescription Fulfillment ──────────────────────────────────────────────

    @Transactional
    public DispenseLogResponse fulfillPrescription(DispenseFulfillRequest request) {
        if (dispenseLogRepository.existsByPrescriptionId(request.getPrescriptionId())) {
            throw new IllegalArgumentException("Prescription already fulfilled: " + request.getPrescriptionId());
        }

        DispenseLog log = DispenseLog.builder()
                .prescriptionId(request.getPrescriptionId())
                .patientId(request.getPatientId())
                .patientName(request.getPatientName())
                .doctorId(request.getDoctorId())
                .doctorName(request.getDoctorName())
                .dispensedBy(request.getDispensedBy())
                .notes(request.getNotes())
                .status(DispenseStatus.PENDING)
                .billingStatus(BillingStatus.UNPAID)
                .totalAmount(0.0)
                .build();

        // Process each dispense item
        double total = 0.0;
        boolean hasOutOfStock = false;

        for (DispenseItemRequest itemReq : request.getItems()) {
            Drug drug = findDrugById(itemReq.getDrugId());

            if (drug.isExpired()) {
                throw new IllegalArgumentException("Drug is expired: " + drug.getName());
            }

            if (drug.getQuantityInStock() < itemReq.getQuantityDispensed()) {
                hasOutOfStock = true;
                this.log.warn("Insufficient stock for drug: {} (requested={}, available={})",
                        drug.getName(), itemReq.getQuantityDispensed(), drug.getQuantityInStock());
            } else {
                drug.setQuantityInStock(drug.getQuantityInStock() - itemReq.getQuantityDispensed());
                drugRepository.save(drug);
            }

            double subtotal = drug.getUnitPrice() * itemReq.getQuantityDispensed();
            total += subtotal;

            DispenseItem item = DispenseItem.builder()
                    .dispenseLog(log)
                    .drug(drug)
                    .medicineName(drug.getName())
                    .quantityDispensed(itemReq.getQuantityDispensed())
                    .unitPrice(drug.getUnitPrice())
                    .subtotal(subtotal)
                    .build();

            log.getItems().add(item);
        }

        log.setTotalAmount(total);
        log.setStatus(hasOutOfStock ? DispenseStatus.PARTIALLY_FILLED : DispenseStatus.DISPENSED);
        log.setDispensedAt(LocalDateTime.now());

        DispenseLog saved = dispenseLogRepository.save(log);
        this.log.info("Prescription {} dispensed (status={})", request.getPrescriptionId(), saved.getStatus());
        return toDispenseLogResponse(saved);
    }

    @Transactional
    public DispenseLogResponse updateBillingStatus(Long dispenseLogId, BillingStatus billingStatus) {
        DispenseLog dispenseLog = dispenseLogRepository.findById(dispenseLogId)
                .orElseThrow(() -> new IllegalArgumentException("Dispense log not found: " + dispenseLogId));
        dispenseLog.setBillingStatus(billingStatus);
        return toDispenseLogResponse(dispenseLogRepository.save(dispenseLog));
    }

    public DispenseLogResponse getDispenseLogById(Long id) {
        return toDispenseLogResponse(dispenseLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dispense log not found: " + id)));
    }

    public DispenseLogResponse getDispenseLogByPrescription(Long prescriptionId) {
        return toDispenseLogResponse(dispenseLogRepository.findByPrescriptionId(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("No dispense log for prescription: " + prescriptionId)));
    }

    public List<DispenseLogResponse> getAllDispenseLogs() {
        return dispenseLogRepository.findAll().stream()
                .map(this::toDispenseLogResponse).collect(Collectors.toList());
    }

    public List<DispenseLogResponse> getDispenseLogsByPatient(Long patientId) {
        return dispenseLogRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream()
                .map(this::toDispenseLogResponse).collect(Collectors.toList());
    }

    public List<DispenseLogResponse> getDispenseLogsByStatus(DispenseStatus status) {
        return dispenseLogRepository.findByStatus(status).stream()
                .map(this::toDispenseLogResponse).collect(Collectors.toList());
    }

    public List<DispenseLogResponse> getDispenseLogsByBillingStatus(BillingStatus billingStatus) {
        return dispenseLogRepository.findByBillingStatus(billingStatus).stream()
                .map(this::toDispenseLogResponse).collect(Collectors.toList());
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    public long countLowStockDrugs() {
        return drugRepository.findLowStockDrugs().size();
    }

    public long countPendingDispense() {
        return dispenseLogRepository.countByStatus(DispenseStatus.PENDING);
    }

    public long countDispensedToday() {
        return dispenseLogRepository.countByStatus(DispenseStatus.DISPENSED);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Drug findDrugById(Long id) {
        return drugRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drug not found: " + id));
    }

    private DrugResponse toDrugResponse(Drug d) {
        return DrugResponse.builder()
                .id(d.getId())
                .name(d.getName())
                .genericName(d.getGenericName())
                .manufacturer(d.getManufacturer())
                .category(d.getCategory())
                .strength(d.getStrength())
                .dosageForm(d.getDosageForm())
                .quantityInStock(d.getQuantityInStock())
                .reorderLevel(d.getReorderLevel())
                .unitPrice(d.getUnitPrice())
                .expiryDate(d.getExpiryDate())
                .batchNumber(d.getBatchNumber())
                .lowStock(d.isLowStock())
                .expired(d.isExpired())
                .createdAt(d.getCreatedAt())
                .build();
    }

    private DispenseLogResponse toDispenseLogResponse(DispenseLog dl) {
        List<DispenseItemResponse> items = dl.getItems().stream()
                .map(i -> DispenseItemResponse.builder()
                        .id(i.getId())
                        .drugId(i.getDrug().getId())
                        .medicineName(i.getMedicineName())
                        .quantityDispensed(i.getQuantityDispensed())
                        .unitPrice(i.getUnitPrice())
                        .subtotal(i.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return DispenseLogResponse.builder()
                .id(dl.getId())
                .prescriptionId(dl.getPrescriptionId())
                .patientId(dl.getPatientId())
                .patientName(dl.getPatientName())
                .doctorId(dl.getDoctorId())
                .doctorName(dl.getDoctorName())
                .status(dl.getStatus())
                .billingStatus(dl.getBillingStatus())
                .totalAmount(dl.getTotalAmount())
                .dispensedBy(dl.getDispensedBy())
                .notes(dl.getNotes())
                .createdAt(dl.getCreatedAt())
                .dispensedAt(dl.getDispensedAt())
                .items(items)
                .build();
    }
}
