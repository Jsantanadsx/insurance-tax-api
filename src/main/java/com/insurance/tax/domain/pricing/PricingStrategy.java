package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

public interface PricingStrategy {

    InsuranceCategory supportedCategory();

    TaxRates taxRates();

    default BigDecimal calculate(BigDecimal basePrice) {
        return taxRates().applyTo(basePrice);
    }
}
