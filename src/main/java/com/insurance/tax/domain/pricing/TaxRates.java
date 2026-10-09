package com.insurance.tax.domain.pricing;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa as taxas utilizadas no cálculo do preço tarifado.
 *
 * As taxas de IOF, PIS e COFINS ficam agrupadas neste objeto
 * para facilitar o uso durante o cálculo.
 *
 * A responsabilidade deste objeto é armazenar e validar as taxas.
 * O cálculo do preço tarifado fica centralizado no PricingCalculator.
 *
 * BigDecimal é utilizado porque estamos trabalhando com valores
 * monetários e precisamos evitar problemas de precisão.
 */
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