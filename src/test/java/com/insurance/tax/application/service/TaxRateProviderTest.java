package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.TaxRateRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxRates;
import com.insurance.tax.domain.pricing.TaxType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxRateProviderTest {

    @Mock
    private TaxRateRepository taxRateRepository;

    private TaxRateProvider taxRateProvider;

    private LocalDateTime referenceDate;

    @BeforeEach
    void setUp() {
        taxRateProvider = new TaxRateProvider(taxRateRepository);

        referenceDate =
                LocalDateTime.of(2026, 6, 1, 0, 0);
    }

    @Test
    void shouldReturnApplicableRatesForCategory() {

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                )
        ).thenReturn(
                Optional.of(
                        createRate(
                                TaxType.IOF,
                                "0.010000"
                        )
                )
        );

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.PIS,
                        referenceDate
                )
        ).thenReturn(
                Optional.of(
                        createRate(
                                TaxType.PIS,
                                "0.022000"
                        )
                )
        );

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.COFINS,
                        referenceDate
                )
        ).thenReturn(
                Optional.of(
                        createRate(
                                TaxType.COFINS,
                                "0.000000"
                        )
                )
        );

        TaxRates result =
                taxRateProvider.getApplicableRates(
                        InsuranceCategory.VIDA,
                        referenceDate
                );

        assertEquals(
                new BigDecimal("0.010000"),
                result.iof()
        );

        assertEquals(
                new BigDecimal("0.022000"),
                result.pis()
        );

        assertEquals(
                new BigDecimal("0.000000"),
                result.cofins()
        );

        verify(taxRateRepository)
                .findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                );

        verify(taxRateRepository)
                .findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.PIS,
                        referenceDate
                );

        verify(taxRateRepository)
                .findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.COFINS,
                        referenceDate
                );
    }

    @Test
    void shouldThrowExceptionWhenIofRateDoesNotExist() {

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                )
        ).thenReturn(Optional.empty());

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> taxRateProvider
                                .getApplicableRates(
                                        InsuranceCategory.VIDA,
                                        referenceDate
                                )
                );

        assertTrue(
                exception.getMessage().contains("IOF")
        );

        assertTrue(
                exception.getMessage().contains("VIDA")
        );
    }

    @Test
    void shouldThrowExceptionWhenPisRateDoesNotExist() {

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                )
        ).thenReturn(
                Optional.of(
                        createRate(
                                TaxType.IOF,
                                "0.010000"
                        )
                )
        );

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.PIS,
                        referenceDate
                )
        ).thenReturn(Optional.empty());

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> taxRateProvider
                                .getApplicableRates(
                                        InsuranceCategory.VIDA,
                                        referenceDate
                                )
                );

        assertTrue(
                exception.getMessage().contains("PIS")
        );
    }

    @Test
    void shouldThrowExceptionWhenCofinsRateDoesNotExist() {

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                )
        ).thenReturn(
                Optional.of(
                        createRate(
                                TaxType.IOF,
                                "0.010000"
                        )
                )
        );

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.PIS,
                        referenceDate
                )
        ).thenReturn(
                Optional.of(
                        createRate(
                                TaxType.PIS,
                                "0.022000"
                        )
                )
        );

        when(
                taxRateRepository.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.COFINS,
                        referenceDate
                )
        ).thenReturn(Optional.empty());

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> taxRateProvider
                                .getApplicableRates(
                                        InsuranceCategory.VIDA,
                                        referenceDate
                                )
                );

        assertTrue(
                exception.getMessage().contains("COFINS")
        );
    }

    private TaxRateVersion createRate(
            TaxType taxType,
            String rate
    ) {

        return new TaxRateVersion(
                UUID.randomUUID(),
                InsuranceCategory.VIDA,
                taxType,
                new BigDecimal(rate),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                "SYSTEM",
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );
    }
}