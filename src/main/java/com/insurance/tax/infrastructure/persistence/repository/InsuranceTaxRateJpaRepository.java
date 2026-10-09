package com.insurance.tax.infrastructure.persistence.repository;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxType;
import com.insurance.tax.infrastructure.persistence.entity.InsuranceTaxRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsuranceTaxRateJpaRepository
        extends JpaRepository<InsuranceTaxRateEntity, UUID> {

    Optional<InsuranceTaxRateEntity>
    findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
            InsuranceCategory insuranceCategory,
            TaxType taxType,
            LocalDateTime referenceDate
    );

    @Query("""
            SELECT r
            FROM InsuranceTaxRateEntity r
            WHERE r.validFrom = (
                SELECT MAX(r2.validFrom)
                FROM InsuranceTaxRateEntity r2
                WHERE r2.insuranceCategory = r.insuranceCategory
                  AND r2.taxType = r.taxType
                  AND r2.validFrom <= :referenceDate
            )
            ORDER BY r.insuranceCategory, r.taxType
            """)
    List<InsuranceTaxRateEntity> findApplicableRates(
            @Param("referenceDate") LocalDateTime referenceDate
    );
}