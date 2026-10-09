package com.insurance.tax.infrastructure.persistence.mapper;

import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.infrastructure.persistence.entity.InsuranceTaxRateEntity;

/**
 * Responsavel por converter os dados entre o dominio
 * e a estrutura utilizada pelo banco de dados.
 *
 * <p>Dessa forma, o dominio nao precisa conhecer
 * detalhes de JPA ou Hibernate.</p>
 */
public final class InsuranceTaxRateMapper {

    private InsuranceTaxRateMapper() {
    }

    public static InsuranceTaxRateEntity toEntity(
            TaxRateVersion taxRateVersion
    ) {
        return new InsuranceTaxRateEntity(
                taxRateVersion.id(),
                taxRateVersion.insuranceCategory(),
                taxRateVersion.taxType(),
                taxRateVersion.rate(),
                taxRateVersion.validFrom(),
                taxRateVersion.createdBy(),
                taxRateVersion.createdAt()
        );
    }

    public static TaxRateVersion toDomain(
            InsuranceTaxRateEntity entity
    ) {
        return new TaxRateVersion(
                entity.getId(),
                entity.getInsuranceCategory(),
                entity.getTaxType(),
                entity.getRate(),
                entity.getValidFrom(),
                entity.getCreatedBy(),
                entity.getCreatedAt()
        );
    }
}