package com.insurance.tax.domain.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record TaxRates(
        BigDecimal iof,
        BigDecimal pis,
        BigDecimal cofins
) {

    public TaxRates {

        validateRate(iof, "IOF");
        validateRate(pis, "PIS");
        validateRate(cofins, "COFINS");
    }

    public BigDecimal applyTo(BigDecimal basePrice) {

        Objects.requireNonNull(
                basePrice,
                "Base price must not be null"
        );

        if (basePrice.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Base price must be greater than zero"
            );
        }

        BigDecimal iofValue = basePrice.multiply(iof);
        BigDecimal pisValue = basePrice.multiply(pis);
        BigDecimal cofinsValue = basePrice.multiply(cofins);

        return basePrice
                .add(iofValue)
                .add(pisValue)
                .add(cofinsValue)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static void validateRate(
            BigDecimal rate,
            String taxName
    ) {

        Objects.requireNonNull(
                rate,
                taxName + " rate must not be null"
        );

        if (rate.signum() < 0) {
            throw new IllegalArgumentException(
                    taxName + " rate must not be negative"
            );
        }
    }
}

