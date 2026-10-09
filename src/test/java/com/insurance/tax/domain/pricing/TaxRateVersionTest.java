package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaxRateVersionTest {

    @Test
    void shouldCreateValidTaxRateVersion() {
        UUID id = UUID.randomUUID();
        LocalDateTime validFrom =
                LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 1, 1, 10, 0);

        TaxRateVersion taxRateVersion =
                new TaxRateVersion(
                        id,
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.010000"),
                        validFrom,
                        "SYSTEM",
                        createdAt
                );

        assertEquals(id, taxRateVersion.id());
        assertEquals(
                InsuranceCategory.VIDA,
                taxRateVersion.insuranceCategory()
        );
        assertEquals(TaxType.IOF, taxRateVersion.taxType());
        assertEquals(
                new BigDecimal("0.010000"),
                taxRateVersion.rate()
        );
        assertEquals(validFrom, taxRateVersion.validFrom());
        assertEquals("SYSTEM", taxRateVersion.createdBy());
        assertEquals(createdAt, taxRateVersion.createdAt());
    }

    @Test
    void shouldAllowZeroTaxRate() {
        TaxRateVersion taxRateVersion =
                createTaxRateVersion(BigDecimal.ZERO);

        assertEquals(
                BigDecimal.ZERO,
                taxRateVersion.rate()
        );
    }

    @Test
    void shouldRejectNegativeTaxRate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createTaxRateVersion(
                        new BigDecimal("-0.01")
                )
        );
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(
                NullPointerException.class,
                () -> new TaxRateVersion(
                        null,
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.01"),
                        LocalDateTime.now(),
                        "SYSTEM",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldRejectNullInsuranceCategory() {
        assertThrows(
                NullPointerException.class,
                () -> new TaxRateVersion(
                        UUID.randomUUID(),
                        null,
                        TaxType.IOF,
                        new BigDecimal("0.01"),
                        LocalDateTime.now(),
                        "SYSTEM",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldRejectNullTaxType() {
        assertThrows(
                NullPointerException.class,
                () -> new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        null,
                        new BigDecimal("0.01"),
                        LocalDateTime.now(),
                        "SYSTEM",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldRejectNullRate() {
        assertThrows(
                NullPointerException.class,
                () -> new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        null,
                        LocalDateTime.now(),
                        "SYSTEM",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldRejectNullValidFrom() {
        assertThrows(
                NullPointerException.class,
                () -> new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.01"),
                        null,
                        "SYSTEM",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldRejectBlankCreatedBy() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.01"),
                        LocalDateTime.now(),
                        "   ",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldRejectNullCreatedBy() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.01"),
                        LocalDateTime.now(),
                        null,
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldRejectNullCreatedAt() {
        assertThrows(
                NullPointerException.class,
                () -> new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.01"),
                        LocalDateTime.now(),
                        "SYSTEM",
                        null
                )
        );
    }

    private TaxRateVersion createTaxRateVersion(
            BigDecimal rate
    ) {
        return new TaxRateVersion(
                UUID.randomUUID(),
                InsuranceCategory.VIDA,
                TaxType.IOF,
                rate,
                LocalDateTime.of(2026, 1, 1, 0, 0),
                "SYSTEM",
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );
    }
}