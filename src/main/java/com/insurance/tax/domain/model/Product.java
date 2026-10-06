package com.insurance.tax.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record Product (
        UUID id,
        String name,
        InsuranceCategory category,
        BigDecimal basePrice,
        BigDecimal tariffedPrice
) {

    public Product {

        Objects.requireNonNull(id, "Product id must not be null");
        Objects.requireNonNull(category, "Product category must not be null");
        Objects.requireNonNull(basePrice, "Base price must not be null");
        Objects.requireNonNull(tariffedPrice, "Tariffed price must not be null");

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name must not be blank"
            );
        }

        if (basePrice.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Base price must be greater than zero"
            );
        }

        if (tariffedPrice.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Tariffed price must be greater than zero"
            );
        }
    }
}

