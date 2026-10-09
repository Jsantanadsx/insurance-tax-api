package com.insurance.tax.infrastructure.persistence.adapter;

import com.insurance.tax.application.port.out.TaxRateRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;
import com.insurance.tax.infrastructure.persistence.mapper.InsuranceTaxRateMapper;
import com.insurance.tax.infrastructure.persistence.repository.InsuranceTaxRateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementa o acesso às taxas utilizando JPA.
 *
 * <p>A camada de aplicação depende apenas de TaxRateRepository.
 * Este adapter é responsável por traduzir esse contrato para
 * a tecnologia de persistência utilizada pela aplicação.</p>
 */
@Repository
@RequiredArgsConstructor
public class TaxRateRepositoryAdapter implements TaxRateRepository {

    private final InsuranceTaxRateJpaRepository jpaRepository;

    @Override
    public Optional<TaxRateVersion> findApplicableRate(
            InsuranceCategory insuranceCategory,
            TaxType taxType,
            LocalDateTime referenceDate
    ) {
        return jpaRepository
                .findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
                        insuranceCategory,
                        taxType,
                        referenceDate
                )
                .map(InsuranceTaxRateMapper::toDomain);
    }

    @Override
    public TaxRateVersion save(
            TaxRateVersion taxRateVersion
    ) {
        var entity =
                InsuranceTaxRateMapper.toEntity(taxRateVersion);

        var savedEntity =
                jpaRepository.save(entity);

        return InsuranceTaxRateMapper.toDomain(savedEntity);
    }

    @Override
    public List<TaxRateVersion> findApplicableRates(
            LocalDateTime referenceDate
    ) {
        return jpaRepository
                .findApplicableRates(referenceDate)
                .stream()
                .map(InsuranceTaxRateMapper::toDomain)
                .toList();
    }
}