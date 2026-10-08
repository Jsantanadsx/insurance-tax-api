package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

/**
 * Define as taxas utilizadas para seguros da categoria RESIDENCIAL.
 *
 * Taxas definidas no desafio:
 * IOF de 4%, PIS de 0% e COFINS de 3%.
 */

public final class ResidentialPricingStrategy
        implements PricingStrategy{

    private static final TaxRates TAX_RATES =
            new TaxRates(
                    new BigDecimal("0.04"),
                    BigDecimal.ZERO,
                    new BigDecimal("0.03")
            );

    @Override
    public InsuranceCategory supportedCategory() {
        return InsuranceCategory.RESIDENCIAL;
    }

    @Override
    public TaxRates taxRates() {
        return TAX_RATES;
    }
}
