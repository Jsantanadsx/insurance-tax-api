package com.insurance.tax.infrastructure.persistence.adapter;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;
import com.insurance.tax.infrastructure.persistence.entity.InsuranceTaxRateEntity;
import com.insurance.tax.infrastructure.persistence.repository.InsuranceTaxRateJpaRepository;
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
class TaxRateRepositoryAdapterTest {

    @Mock
    private InsuranceTaxRateJpaRepository jpaRepository;

    private TaxRateRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TaxRateRepositoryAdapter(jpaRepository);
    }

    @Test
    void shouldFindApplicableRate() {
        UUID id = UUID.randomUUID();

        InsuranceTaxRateEntity entity =
                new InsuranceTaxRateEntity(
                        id,
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.010000"),
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        "SYSTEM",
                        LocalDateTime.of(2026, 1, 1, 10, 0)
                );

        LocalDateTime referenceDate =
                LocalDateTime.of(2026, 6, 1, 0, 0);

        when(
                jpaRepository
                        .findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
                                InsuranceCategory.VIDA,
                                TaxType.IOF,
                                referenceDate
                        )
        ).thenReturn(Optional.of(entity));

        Optional<TaxRateVersion> result =
                adapter.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                );

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
        assertEquals(
                InsuranceCategory.VIDA,
                result.get().insuranceCategory()
        );
        assertEquals(
                TaxType.IOF,
                result.get().taxType()
        );
        assertEquals(
                new BigDecimal("0.010000"),
                result.get().rate()
        );

        verify(jpaRepository)
                .findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                );
    }

    @Test
    void shouldReturnEmptyWhenApplicableRateDoesNotExist() {
        LocalDateTime referenceDate =
                LocalDateTime.of(2025, 1, 1, 0, 0);

        when(
                jpaRepository
                        .findTopByInsuranceCategoryAndTaxTypeAndValidFromLessThanEqualOrderByValidFromDesc(
                                InsuranceCategory.VIDA,
                                TaxType.IOF,
                                referenceDate
                        )
        ).thenReturn(Optional.empty());

        Optional<TaxRateVersion> result =
                adapter.findApplicableRate(
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        referenceDate
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveTaxRateVersion() {
        UUID id = UUID.randomUUID();
        LocalDateTime validFrom =
                LocalDateTime.of(2027, 1, 1, 0, 0);
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 12, 20, 10, 0);

        TaxRateVersion domain =
                new TaxRateVersion(
                        id,
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.015000"),
                        validFrom,
                        "admin",
                        createdAt
                );

        InsuranceTaxRateEntity savedEntity =
                new InsuranceTaxRateEntity(
                        id,
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.015000"),
                        validFrom,
                        "admin",
                        createdAt
                );

        when(jpaRepository.save(any(InsuranceTaxRateEntity.class)))
                .thenReturn(savedEntity);

        TaxRateVersion result =
                adapter.save(domain);

        assertEquals(domain, result);

        verify(jpaRepository)
                .save(any(InsuranceTaxRateEntity.class));
    }
}