package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

/**
 * Define as taxas utilizadas para seguros da categoria AUTO.
 *
 * Taxas definidas no desafio:
 * IOF de 5,5%, PIS de 4% e COFINS de 1%.
 */

public final class AutoPricingStrategy
        implements PricingStrategy {

    private static final TaxRates TAX_RATES =
            new TaxRates(
                    new BigDecimal("0.055"),
                    new BigDecimal("0.04"),
                    new BigDecimal("0.01")
            );

    @Override
    public InsuranceCategory supportedCategory() {
        return InsuranceCategory.AUTO;
    }

    @Override
    public TaxRates taxRates() {
        return TAX_RATES;
    }
}
