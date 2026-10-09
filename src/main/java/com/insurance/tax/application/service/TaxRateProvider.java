package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.TaxRateRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxRates;
import com.insurance.tax.domain.pricing.TaxType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Responsavel por fornecer o conjunto de taxas vigente
 * para uma categoria de seguro em uma determinada data.
 *
 * As taxas sao obtidas atraves de TaxRateRepository,
 * mantendo a regra de negocio independente da tecnologia
 * utilizada para armazenamento.
 */
@Service
@RequiredArgsConstructor
public class TaxRateProvider {

    private final TaxRateRepository taxRateRepository;

    public TaxRates getApplicableRates(
            InsuranceCategory insuranceCategory,
            LocalDateTime referenceDate
    ) {

        BigDecimal iof = findRate(
                insuranceCategory,
                TaxType.IOF,
                referenceDate
        );

        BigDecimal pis = findRate(
                insuranceCategory,
                TaxType.PIS,
                referenceDate
        );

        BigDecimal cofins = findRate(
                insuranceCategory,
                TaxType.COFINS,
                referenceDate
        );

        return new TaxRates(
                iof,
                pis,
                cofins
        );
    }

    private BigDecimal findRate(
            InsuranceCategory insuranceCategory,
            TaxType taxType,
            LocalDateTime referenceDate
    ) {

        return taxRateRepository
                .findApplicableRate(
                        insuranceCategory,
                        taxType,
                        referenceDate
                )
                .map(TaxRateVersion::rate)
                .orElseThrow(
                        () -> new IllegalStateException(
                                "No applicable "
                                        + taxType
                                        + " rate found for category "
                                        + insuranceCategory
                                        + " at "
                                        + referenceDate
                        )
                );
    }
}