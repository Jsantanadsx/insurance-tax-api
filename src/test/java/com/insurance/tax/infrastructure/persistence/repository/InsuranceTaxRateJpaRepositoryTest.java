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
}