package com.insurance.tax.domain.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Responsavel por calcular o preco final do seguro
 * a partir do preco base e das taxas aplicaveis.
 *
 * A classe nao sabe de onde as taxas vieram.
 * Ela apenas recebe os valores e aplica a formula
 * de tarifacao.
 */
public class PricingCalculator {

    public BigDecimal calculate(
            BigDecimal basePrice,
            TaxRates taxRates
    ) {

        Objects.requireNonNull(
                basePrice,
                "Base price must not be null"
        );

        Objects.requireNonNull(
                taxRates,
                "Tax rates must not be null"
        );

        if (basePrice.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Base price must be greater than zero"
            );
        }

        BigDecimal iofValue =
                basePrice.multiply(taxRates.iof());

        BigDecimal pisValue =
                basePrice.multiply(taxRates.pis());

        BigDecimal cofinsValue =
                basePrice.multiply(taxRates.cofins());

        return basePrice
                .add(iofValue)
                .add(pisValue)
                .add(cofinsValue)
                .setScale(2, RoundingMode.HALF_UP);
    }
}