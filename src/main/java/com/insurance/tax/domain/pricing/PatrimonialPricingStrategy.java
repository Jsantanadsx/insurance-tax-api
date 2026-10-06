package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

public final class PatrimonialPricingStrategy
        implements PricingStrategy {

    private static final TaxRates TAX_RATES =
            new TaxRates(
                    new BigDecimal("0.05"),
                    new BigDecimal("0.03"),
                    BigDecimal.ZERO
            );

    @Override
    public InsuranceCategory supportedCategory() {
        return InsuranceCategory.PATRIMONIAL;
    }

    @Override
    public TaxRates taxRates() {
        return TAX_RATES;
    }
}