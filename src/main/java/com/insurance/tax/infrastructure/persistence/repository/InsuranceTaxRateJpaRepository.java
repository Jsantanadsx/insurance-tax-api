package com.insurance.tax.infrastructure.persistence.repository;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxType;
import com.insurance.tax.infrastructure.persistence.entity.InsuranceTaxRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Acesso aos registros de taxas armazenados no banco.
 *
 * A consulta principal busca a versão mais recente de uma taxa
 * que já esteja válida na data informada.
 */
public interface InsuranceTaxRateJpaRepository
        extends JpaRepository<InsuranceTaxRateEntity, UUID> {

    Optional<InsuranceTaxRateEntity>
    findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
            InsuranceCategory insuranceCategory,
            TaxType taxType,
            LocalDateTime referenceDate
    );
}