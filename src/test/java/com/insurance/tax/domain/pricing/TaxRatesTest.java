package com.insurance.tax.domain.pricing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxRatesTest {

    @Test
    void shouldCreateValidTaxRates() {

        TaxRates taxRates =
                new TaxRates(
                        new BigDecimal("0.01"),
                        new BigDecimal("0.022"),
                        BigDecimal.ZERO
                );

        assertEquals(
                new BigDecimal("0.01"),
                taxRates.iof()
        );

        assertEquals(
                new BigDecimal("0.022"),
                taxRates.pis()
        );

        assertEquals(
                BigDecimal.ZERO,
                taxRates.cofins()
        );
    }

    @Test
    void shouldRejectNullIofRate() {

        assertThrows(
                NullPointerException.class,
                () -> new TaxRates(
                        null,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldRejectNullPisRate() {

        assertThrows(
                NullPointerException.class,
                () -> new TaxRates(
                        BigDecimal.ZERO,
                        null,
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldRejectNullCofinsRate() {

        assertThrows(
                NullPointerException.class,
                () -> new TaxRates(
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        null
                )
        );
    }

    @Test
    void shouldRejectNegativeIofRate() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TaxRates(
                        new BigDecimal("-0.01"),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldRejectNegativePisRate() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TaxRates(
                        BigDecimal.ZERO,
                        new BigDecimal("-0.01"),
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldRejectNegativeCofinsRate() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TaxRates(
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        new BigDecimal("-0.01")
                )
        );
    }
}