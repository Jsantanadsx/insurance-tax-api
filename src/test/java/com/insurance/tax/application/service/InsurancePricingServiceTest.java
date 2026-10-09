package com.insurance.tax.application.service;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.PricingCalculator;
import com.insurance.tax.domain.pricing.TaxRates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsurancePricingServiceTest {

    @Mock
    private TaxRateProvider taxRateProvider;

    private InsurancePricingService insurancePricingService;

    private LocalDateTime referenceDate;

    @BeforeEach
    void setUp() {
        insurancePricingService =
                new InsurancePricingService(
                        taxRateProvider,
                        new PricingCalculator()
                );

        referenceDate =
                LocalDateTime.of(2026, 6, 1, 0, 0);
    }

    @Test
    void shouldCalculatePriceUsingApplicableRates() {

        TaxRates rates = new TaxRates(
                new BigDecimal("0.010000"),
                new BigDecimal("0.022000"),
                new BigDecimal("0.000000")
        );

        when(
                taxRateProvider.getApplicableRates(
                        InsuranceCategory.VIDA,
                        referenceDate
                )
        ).thenReturn(rates);

        BigDecimal result =
                insurancePricingService.calculatePrice(
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        referenceDate
                );

        assertEquals(
                new BigDecimal("103.20"),
                result
        );

        verify(taxRateProvider)
                .getApplicableRates(
                        InsuranceCategory.VIDA,
                        referenceDate
                );
    }

    @Test
    void shouldReflectNewRatesWithoutChangingCalculationLogic() {

        TaxRates newRates = new TaxRates(
                new BigDecimal("0.020000"),
                new BigDecimal("0.030000"),
                new BigDecimal("0.010000")
        );

        when(
                taxRateProvider.getApplicableRates(
                        InsuranceCategory.VIDA,
                        referenceDate
                )
        ).thenReturn(newRates);

        BigDecimal result =
                insurancePricingService.calculatePrice(
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        referenceDate
                );

        assertEquals(
                new BigDecimal("106.00"),
                result
        );
    }

    @Test
    void shouldNotCalculateWhenRatesCannotBeObtained() {

        when(
                taxRateProvider.getApplicableRates(
                        InsuranceCategory.VIDA,
                        referenceDate
                )
        ).thenThrow(
                new IllegalStateException(
                        "No applicable IOF rate found"
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> insurancePricingService.calculatePrice(
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        referenceDate
                )
        );
    }
}