package com.insurance.tax.application.service;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.PricingCalculator;
import com.insurance.tax.domain.pricing.TaxRates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Coordena o calculo do preco tarifado de um seguro.
 *
 * Primeiro busca as taxas vigentes para a categoria
 * e para a data informada. Depois utiliza essas taxas
 * para realizar o calculo do preco final.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InsurancePricingService {

    private final TaxRateProvider taxRateProvider;
    private final PricingCalculator pricingCalculator;

    public BigDecimal calculatePrice(
            InsuranceCategory insuranceCategory,
            BigDecimal basePrice,
            LocalDateTime referenceDate
    ) {

        log.debug(
                "Calculando preco tarifado. categoria={}, precoBase={}, dataReferencia={}",
                insuranceCategory,
                basePrice,
                referenceDate
        );

        TaxRates taxRates =
                taxRateProvider.getApplicableRates(
                        insuranceCategory,
                        referenceDate
                );

        BigDecimal tariffedPrice =
                pricingCalculator.calculate(
                        basePrice,
                        taxRates
                );

        log.debug(
                "Preco tarifado calculado. categoria={}, precoBase={}, precoTarifado={}",
                insuranceCategory,
                basePrice,
                tariffedPrice
        );

        return tariffedPrice;
    }
}