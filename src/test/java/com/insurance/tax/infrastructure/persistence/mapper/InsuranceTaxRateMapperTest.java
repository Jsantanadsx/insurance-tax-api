package com.insurance.tax.infrastructure.persistence.mapper;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;
import com.insurance.tax.infrastructure.persistence.entity.InsuranceTaxRateEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsuranceTaxRateMapperTest {

    @Test
    void shouldConvertDomainToEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime validFrom =
                LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 1, 1, 10, 0);

        TaxRateVersion domain =
                new TaxRateVersion(
                        id,
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.010000"),
                        validFrom,
                        "SYSTEM",
                        createdAt
                );

        InsuranceTaxRateEntity entity =
                InsuranceTaxRateMapper.toEntity(domain);

        assertEquals(id, entity.getId());
        assertEquals(
                InsuranceCategory.VIDA,
                entity.getInsuranceCategory()
        );
        assertEquals(TaxType.IOF, entity.getTaxType());
        assertEquals(
                new BigDecimal("0.010000"),
                entity.getRate()
        );
        assertEquals(validFrom, entity.getValidFrom());
        assertEquals("SYSTEM", entity.getCreatedBy());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    void shouldConvertEntityToDomain() {
        UUID id = UUID.randomUUID();
        LocalDateTime validFrom =
                LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 1, 1, 10, 0);

        InsuranceTaxRateEntity entity =
                new InsuranceTaxRateEntity(
                        id,
                        InsuranceCategory.AUTO,
                        TaxType.PIS,
                        new BigDecimal("0.040000"),
                        validFrom,
                        "SYSTEM",
                        createdAt
                );

        TaxRateVersion domain =
                InsuranceTaxRateMapper.toDomain(entity);

        assertEquals(id, domain.id());
        assertEquals(
                InsuranceCategory.AUTO,
                domain.insuranceCategory()
        );
        assertEquals(TaxType.PIS, domain.taxType());
        assertEquals(
                new BigDecimal("0.040000"),
                domain.rate()
        );
        assertEquals(validFrom, domain.validFrom());
        assertEquals("SYSTEM", domain.createdBy());
        assertEquals(createdAt, domain.createdAt());
    }

    @Test
    void shouldPreserveDataInRoundTripConversion() {
        TaxRateVersion original =
                new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.PATRIMONIAL,
                        TaxType.COFINS,
                        BigDecimal.ZERO,
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        "SYSTEM",
                        LocalDateTime.of(2026, 1, 1, 10, 0)
                );

        InsuranceTaxRateEntity entity =
                InsuranceTaxRateMapper.toEntity(original);

        TaxRateVersion result =
                InsuranceTaxRateMapper.toDomain(entity);

        assertEquals(original, result);
    }
}