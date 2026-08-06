package com.pms.pharmacy.repository;

import com.pms.pharmacy.entity.Drug;
import com.pms.pharmacy.entity.DrugCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    List<Drug> findByCategory(DrugCategory category);

    @Query("SELECT d FROM Drug d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(d.genericName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Drug> searchByName(@Param("name") String name);

    /** Drugs at or below reorder level */
    @Query("SELECT d FROM Drug d WHERE d.quantityInStock <= d.reorderLevel")
    List<Drug> findLowStockDrugs();

    /** Drugs expiring on or before the given date */
    List<Drug> findByExpiryDateLessThanEqual(LocalDate date);

    boolean existsByNameAndStrengthAndDosageForm(String name, String strength, String dosageForm);
}
