package com.insurance.tax.infrastructure.persistence.repository;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxType;
import com.insurance.tax.infrastructure.persistence.entity.InsuranceTaxRateEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class InsuranceTaxRateJpaRepositoryTest {

    @Autowired
    private InsuranceTaxRateJpaRepository repository;

    @Test
    void shouldReturnRateValidAtReferenceDate() {

        InsuranceTaxRateEntity futureRate =
                new InsuranceTaxRateEntity(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.015000"),
                        LocalDateTime.of(2027, 1, 1, 0, 0),
                        "admin",
                        LocalDateTime.of(2026, 12, 20, 10, 0)
                );

        repository.saveAndFlush(futureRate);

        Optional<InsuranceTaxRateEntity> beforeChange =
                repository
                        .findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
                                InsuranceCategory.VIDA,
                                TaxType.IOF,
                                LocalDateTime.of(2026, 6, 1, 0, 0)
                        );

        Optional<InsuranceTaxRateEntity> afterChange =
                repository
                        .findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
                                InsuranceCategory.VIDA,
                                TaxType.IOF,
                                LocalDateTime.of(2027, 3, 1, 0, 0)
                        );

        assertTrue(beforeChange.isPresent());
        assertEquals(
                new BigDecimal("0.010000"),
                beforeChange.get().getRate()
        );

        assertTrue(afterChange.isPresent());
        assertEquals(
                new BigDecimal("0.015000"),
                afterChange.get().getRate()
        );
    }

    @Test
    void shouldReturnEmptyWhenNoRateIsValidYet() {

        Optional<InsuranceTaxRateEntity> result =
                repository
                        .findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
                                InsuranceCategory.VIDA,
                                TaxType.IOF,
                                LocalDateTime.of(2025, 1, 1, 0, 0)
                        );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnOnlyApplicableRatesAtReferenceDate() {

        InsuranceTaxRateEntity futureRate =
                new InsuranceTaxRateEntity(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.020000"),
                        LocalDateTime.of(2027, 1, 1, 0, 0),
                        "admin",
                        LocalDateTime.of(2026, 12, 20, 10, 0)
                );

        repository.saveAndFlush(futureRate);

        List<InsuranceTaxRateEntity> ratesIn2026 =
                repository.findApplicableRates(
                        LocalDateTime.of(2026, 6, 1, 0, 0)
                );

        List<InsuranceTaxRateEntity> ratesIn2027 =
                repository.findApplicableRates(
                        LocalDateTime.of(2027, 6, 1, 0, 0)
                );

        assertEquals(15, ratesIn2026.size());
        assertEquals(15, ratesIn2027.size());

        InsuranceTaxRateEntity vidaIof2026 =
                ratesIn2026.stream()
                        .filter(rate ->
                                rate.getInsuranceCategory()
                                        == InsuranceCategory.VIDA
                                        && rate.getTaxType()
                                        == TaxType.IOF
                        )
                        .findFirst()
                        .orElseThrow();

        InsuranceTaxRateEntity vidaIof2027 =
                ratesIn2027.stream()
                        .filter(rate ->
                                rate.getInsuranceCategory()
                                        == InsuranceCategory.VIDA
                                        && rate.getTaxType()
                                        == TaxType.IOF
                        )
                        .findFirst()
                        .orElseThrow();

        assertEquals(
                new BigDecimal("0.010000"),
                vidaIof2026.getRate()
        );

        assertEquals(
                new BigDecimal("0.020000"),
                vidaIof2027.getRate()
        );
    }
}