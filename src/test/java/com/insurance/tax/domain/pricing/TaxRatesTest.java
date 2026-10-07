package com.insurance.tax.domain.pricing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxRatesTest {

    @Test
    void shouldCalculateTariffedPriced() {
        TaxRates taxRates = new TaxRates(
                new BigDecimal("0.01"),
                new BigDecimal("0.022"),
                BigDecimal.ZERO
        );

        BigDecimal result = taxRates.applyTo(
                new BigDecimal("100.00")
        );

        assertEquals(
                new BigDecimal("103.20"),
                result
        );
    }

    @Test
    void shouldRoundTariffedPriceToTwoDecimalPlaces(){
        TaxRates taxRates = new TaxRates(
                new BigDecimal("0.055"),
                new BigDecimal("0.04"),
                new BigDecimal("0.01")
        );

        BigDecimal result = taxRates.applyTo(
                new BigDecimal("99.99")
        );

        assertEquals(2,result.scale());
    }

    @Test
    void shouldRejectNullBasePrice() {
        TaxRates taxRates = new TaxRates(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThrows(
                NullPointerException.class,
                () -> taxRates.applyTo(null)
        );
    }

    @Test
    void shouldRejectZeroBasePrice() {
        TaxRates taxRates = new TaxRates(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> taxRates.applyTo(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectNegativeBasePrice() {
        TaxRates taxRates = new TaxRates(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> taxRates.applyTo(new BigDecimal("-10.00"))
        );
    }

    @Test
    void shouldRejectNullTaxRate() {
        assertThrows(
                NullPointerException.class,
                () -> new TaxRates(
                        null,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                )
        );
    }
}