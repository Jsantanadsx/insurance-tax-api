package com.insurance.tax.domain.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PricingCalculatorTest {

    private PricingCalculator pricingCalculator;

    @BeforeEach
    void setUp() {
        pricingCalculator = new PricingCalculator();
    }

    @Test
    void shouldCalculateLifeInsurancePrice() {
        TaxRates rates = new TaxRates(
                new BigDecimal("0.010000"),
                new BigDecimal("0.022000"),
                new BigDecimal("0.000000")
        );

        BigDecimal result = pricingCalculator.calculate(
                new BigDecimal("100.00"),
                rates
        );

        assertEquals(
                new BigDecimal("103.20"),
                result
        );
    }

    @Test
    void shouldCalculateAutoInsurancePrice() {
        TaxRates rates = new TaxRates(
                new BigDecimal("0.055000"),
                new BigDecimal("0.040000"),
                new BigDecimal("0.010000")
        );

        BigDecimal result = pricingCalculator.calculate(
                new BigDecimal("50.00"),
                rates
        );

        assertEquals(
                new BigDecimal("55.25"),
                result
        );
    }

    @Test
    void shouldRoundPriceToTwoDecimalPlaces() {
        TaxRates rates = new TaxRates(
                new BigDecimal("0.015000"),
                new BigDecimal("0.022000"),
                BigDecimal.ZERO
        );

        BigDecimal result = pricingCalculator.calculate(
                new BigDecimal("99.99"),
                rates
        );

        assertEquals(
                new BigDecimal("103.69"),
                result
        );
    }

    @Test
    void shouldRejectZeroBasePrice() {
        TaxRates rates = new TaxRates(
                new BigDecimal("0.010000"),
                new BigDecimal("0.022000"),
                BigDecimal.ZERO
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> pricingCalculator.calculate(
                        BigDecimal.ZERO,
                        rates
                )
        );
    }

    @Test
    void shouldRejectNegativeBasePrice() {
        TaxRates rates = new TaxRates(
                new BigDecimal("0.010000"),
                new BigDecimal("0.022000"),
                BigDecimal.ZERO
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> pricingCalculator.calculate(
                        new BigDecimal("-100.00"),
                        rates
                )
        );
    }

    @Test
    void shouldRejectNullBasePrice() {
        TaxRates rates = new TaxRates(
                new BigDecimal("0.010000"),
                new BigDecimal("0.022000"),
                BigDecimal.ZERO
        );

        assertThrows(
                NullPointerException.class,
                () -> pricingCalculator.calculate(
                        null,
                        rates
                )
        );
    }

    @Test
    void shouldRejectNullTaxRates() {
        assertThrows(
                NullPointerException.class,
                () -> pricingCalculator.calculate(
                        new BigDecimal("100.00"),
                        null
                )
        );
    }
}