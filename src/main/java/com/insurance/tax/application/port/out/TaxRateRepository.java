package com.insurance.tax.application.port.out;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Define as operacoes de acesso às taxas necessárias
 * para a regra de negócio.
 *
 * A aplicação conhece apenas este contrato e não precisa
 * saber se os dados estão em H2, PostgreSQL ou outro banco.
 */
public interface TaxRateRepository {

    Optional<TaxRateVersion> findApplicableRate(
            InsuranceCategory insuranceCategory,
            TaxType taxType,
            LocalDateTime referenceDate
    );

    TaxRateVersion save(TaxRateVersion taxRateVersion);
}