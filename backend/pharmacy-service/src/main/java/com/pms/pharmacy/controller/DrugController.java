package com.pms.pharmacy.controller;

import com.pms.pharmacy.dto.DrugRequest;
import com.pms.pharmacy.dto.DrugResponse;
import com.pms.pharmacy.dto.StockUpdateRequest;
import com.pms.pharmacy.entity.DrugCategory;
import com.pms.pharmacy.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacy/drugs")
@RequiredArgsConstructor
public class DrugController {

    private final PharmacyService pharmacyService;

    @PostMapping
    public ResponseEntity<DrugResponse> addDrug(@Valid @RequestBody DrugRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.addDrug(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrugResponse> updateDrug(
            @PathVariable Long id, @Valid @RequestBody DrugRequest request) {
        return ResponseEntity.ok(pharmacyService.updateDrug(id, request));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<DrugResponse> updateStock(
            @PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(pharmacyService.updateStock(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrugResponse> getDrug(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getDrugById(id));
    }

    @GetMapping
    public ResponseEntity<List<DrugResponse>> getAllDrugs() {
        return ResponseEntity.ok(pharmacyService.getAllDrugs());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<DrugResponse>> getByCategory(@PathVariable DrugCategory category) {
        return ResponseEntity.ok(pharmacyService.getDrugsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DrugResponse>> searchDrugs(@RequestParam String name) {
        return ResponseEntity.ok(pharmacyService.searchDrugs(name));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<DrugResponse>> getLowStock() {
        return ResponseEntity.ok(pharmacyService.getLowStockDrugs());
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<DrugResponse>> getExpiring(
            @RequestParam(defaultValue = "30") int daysAhead) {
        return ResponseEntity.ok(pharmacyService.getExpiredOrExpiringDrugs(daysAhead));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrug(@PathVariable Long id) {
        pharmacyService.deleteDrug(id);
        return ResponseEntity.noContent().build();
    }
}
