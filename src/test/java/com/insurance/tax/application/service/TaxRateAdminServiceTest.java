package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.TaxRateRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxRateAdminServiceTest {

    @Mock
    private TaxRateRepository taxRateRepository;

    private TaxRateAdminService taxRateAdminService;

    @BeforeEach
    void setUp() {
        taxRateAdminService =
                new TaxRateAdminService(taxRateRepository);
    }

    @Test
    void shouldReturnApplicableRates() {

        LocalDateTime referenceDate =
                LocalDateTime.of(2026, 6, 1, 0, 0);

        TaxRateVersion vidaIof =
                new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.010000"),
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        "SYSTEM",
                        LocalDateTime.of(2026, 1, 1, 10, 0)
                );

        TaxRateVersion autoIof =
                new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.AUTO,
                        TaxType.IOF,
                        new BigDecimal("0.055000"),
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        "SYSTEM",
                        LocalDateTime.of(2026, 1, 1, 10, 0)
                );

        when(
                taxRateRepository.findApplicableRates(
                        referenceDate
                )
        ).thenReturn(
                List.of(
                        vidaIof,
                        autoIof
                )
        );

        List<TaxRateVersion> result =
                taxRateAdminService.findApplicableRates(
                        referenceDate
                );

        assertEquals(2, result.size());

        assertEquals(
                InsuranceCategory.VIDA,
                result.get(0).insuranceCategory()
        );

        assertEquals(
                InsuranceCategory.AUTO,
                result.get(1).insuranceCategory()
        );

        verify(taxRateRepository)
                .findApplicableRates(referenceDate);
    }

    @Test
    void shouldRejectNullReferenceDate() {

        assertThrows(
                NullPointerException.class,
                () -> taxRateAdminService
                        .findApplicableRates(null)
        );

        verifyNoInteractions(taxRateRepository);
    }
}