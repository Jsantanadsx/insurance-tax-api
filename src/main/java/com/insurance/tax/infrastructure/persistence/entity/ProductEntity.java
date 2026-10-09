package com.insurance.tax.infrastructure.persistence.entity;

import com.insurance.tax.domain.model.InsuranceCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representa a forma como um produto de seguro
 * é armazenado no banco de dados.
 *
 * Esta classe pertence à camada de persistência.
 * As regras de negócio continuam concentradas no
 * objeto Product do domínio.
 */
@Entity
@Table(name = "insurance_product")
public class ProductEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceCategory category;

    @Column(
            name = "base_price",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal basePrice;

    @Column(
            name = "tariffed_price",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal tariffedPrice;

    protected ProductEntity() {
        // Construtor exigido pelo JPA
    }

    public ProductEntity(
            UUID id,
            String name,
            InsuranceCategory category,
            BigDecimal basePrice,
            BigDecimal tariffedPrice
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
        this.tariffedPrice = tariffedPrice;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public InsuranceCategory getCategory() {
        return category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getTariffedPrice() {
        return tariffedPrice;
    }
}