package com.insurance.tax.presentation.dto.response;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxRateResponseTest {

    @Test
    void shouldConvertTaxRateVersionToPortugueseResponse() {

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

        TaxRateResponse response =
                TaxRateResponse.from(taxRateVersion);

        assertEquals(id, response.id());
        assertEquals(
                "VIDA",
                response.categoriaSeguro()
        );
        assertEquals(
                "IOF",
                response.tipoImposto()
        );
        assertEquals(
                new BigDecimal("0.010000"),
                response.taxa()
        );
        assertEquals(
                validFrom,
                response.vigenteDesde()
        );
        assertEquals(
                "SYSTEM",
                response.criadoPor()
        );
        assertEquals(
                createdAt,
                response.criadoEm()
        );
    }
}