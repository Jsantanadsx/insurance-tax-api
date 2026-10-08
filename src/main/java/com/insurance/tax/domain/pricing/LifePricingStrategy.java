package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

/**
 * Define as taxas utilizadas para seguros da categoria VIDA.
 *
 * Taxas definidas no desafio:
 * IOF de 1%, PIS de 2,2% e COFINS de 0%.
 */

public final class LifePricingStrategy
        implements PricingStrategy {

    private static final TaxRates TAX_RATES =
            new TaxRates(
                    new BigDecimal("0.01"),
                    new BigDecimal("0.022"),
                    BigDecimal.ZERO
            );

    @Override
    public InsuranceCategory supportedCategory() {
        return InsuranceCategory.VIDA;
    }

    @Override
    public TaxRates taxRates() {
        return TAX_RATES;
    }
}
