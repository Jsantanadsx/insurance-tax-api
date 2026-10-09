package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.TaxRateRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InsurancePricingIntegrationTest {

    @Autowired
    private InsurancePricingService insurancePricingService;

    @Autowired
    private TaxRateRepository taxRateRepository;

    @Test
    void shouldCalculateLifeInsuranceUsingRatesFromDatabase() {

        BigDecimal result =
                insurancePricingService.calculatePrice(
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        LocalDateTime.of(2026, 6, 1, 0, 0)
                );

        assertEquals(
                new BigDecimal("103.20"),
                result
        );
    }

    @Test
    void shouldCalculateAutoInsuranceUsingRatesFromDatabase() {

        BigDecimal result =
                insurancePricingService.calculatePrice(
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        LocalDateTime.of(2026, 6, 1, 0, 0)
                );

        assertEquals(
                new BigDecimal("55.25"),
                result
        );
    }

    @Test
    void shouldUseNewRateAfterItsValidityDate() {

        TaxRateVersion newIofRate =
                new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.020000"),
                        LocalDateTime.of(2027, 1, 1, 0, 0),
                        "admin",
                        LocalDateTime.of(2026, 12, 20, 10, 0)
                );

        taxRateRepository.save(newIofRate);

        BigDecimal beforeChange =
                insurancePricingService.calculatePrice(
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        LocalDateTime.of(2026, 12, 31, 23, 59)
                );

        BigDecimal afterChange =
                insurancePricingService.calculatePrice(
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        LocalDateTime.of(2027, 1, 1, 0, 0)
                );

        assertEquals(
                new BigDecimal("103.20"),
                beforeChange
        );

        assertEquals(
                new BigDecimal("104.20"),
                afterChange
        );
    }
}