package com.insurance.tax.application.port.out;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Define as operacoes de acesso as taxas necessarias
 * para a regra de negocio.
 *
 * <p>A aplicacao conhece apenas este contrato e nao precisa
 * saber qual tecnologia esta sendo utilizada para armazenar
 * os dados.</p>
 */
public interface TaxRateRepository {

    Optional<TaxRateVersion> findApplicableRate(
            InsuranceCategory insuranceCategory,
            TaxType taxType,
            LocalDateTime referenceDate
    );

    List<TaxRateVersion> findApplicableRates(
            LocalDateTime referenceDate
    );

    TaxRateVersion save(
            TaxRateVersion taxRateVersion
    );
}