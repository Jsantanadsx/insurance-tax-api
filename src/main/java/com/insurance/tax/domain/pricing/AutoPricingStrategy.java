package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

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
