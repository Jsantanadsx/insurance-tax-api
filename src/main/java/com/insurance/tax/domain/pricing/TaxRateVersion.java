package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa uma versão de uma taxa aplicada a uma categoria de seguro.
 *
 * <p>Cada nova alteração de taxa gera um novo registro,
 * preservando as versões anteriores e permitindo consultar
 * o histórico ao longo do tempo.</p>
 */
public record TaxRateVersion(
        UUID id,
        InsuranceCategory insuranceCategory,
        TaxType taxType,
        BigDecimal rate,
        LocalDateTime validFrom,
        String createdBy,
        LocalDateTime createdAt
) {

    public TaxRateVersion {

        Objects.requireNonNull(id, "Tax rate id must not be null");
        Objects.requireNonNull(
                insuranceCategory,
                "Insurance category must not be null"
        );
        Objects.requireNonNull(
                taxType,
                "Tax type must not be null"
        );
        Objects.requireNonNull(
                rate,
                "Tax rate must not be null"
        );
        Objects.requireNonNull(
                validFrom,
                "Valid from must not be null"
        );
        Objects.requireNonNull(
                createdAt,
                "Created at must not be null"
        );

        if (rate.signum() < 0) {
            throw new IllegalArgumentException(
                    "Tax rate must not be negative"
            );
        }

        if (createdBy == null || createdBy.isBlank()) {
            throw new IllegalArgumentException(
                    "Created by must not be blank"
            );
        }
    }
}