package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

/**
 * Define as taxas utilizadas para seguros da categoria VIAGEM.
 *
 * Taxas definidas no desafio:
 * IOF de 2%, PIS de 4% e COFINS de 1%.
 */

public final class TravelPricingStrategy
        implements PricingStrategy {

    private static final TaxRates TAX_RATES =
            new TaxRates(
                    new BigDecimal("0.02"),
                    new BigDecimal("0.04"),
                    new BigDecimal("0.01")
            );

    @Override
    public InsuranceCategory supportedCategory() {
        return InsuranceCategory.VIAGEM;
    }

    @Override
    public TaxRates taxRates() {
        return TAX_RATES;
    }
}
