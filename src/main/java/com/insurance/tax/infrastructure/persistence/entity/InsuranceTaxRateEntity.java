package com.insurance.tax.infrastructure.persistence.entity;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa a tabela responsável por armazenar
 * todas as versões das taxas dos seguros.
 *
 * Cada alteração de taxa cria um novo registro,
 * preservando o histórico das versões anteriores.
 */
@Entity
@Table(
        name = "insurance_tax_rate",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tax_rate_version",
                        columnNames = {
                                "insurance_category",
                                "tax_type",
                                "valid_from"
                        }
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceTaxRateEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "insurance_category",
            nullable = false,
            length = 30
    )
    private InsuranceCategory insuranceCategory;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "tax_type",
            nullable = false,
            length = 20
    )
    private TaxType taxType;

    @Column(
            name = "rate",
            nullable = false,
            precision = 10,
            scale = 6
    )
    private BigDecimal rate;

    @Column(
            name = "valid_from",
            nullable = false
    )
    private LocalDateTime validFrom;

    @Column(
            name = "created_by",
            nullable = false,
            length = 100
    )
    private String createdBy;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    public InsuranceTaxRateEntity(
            UUID id,
            InsuranceCategory insuranceCategory,
            TaxType taxType,
            BigDecimal rate,
            LocalDateTime validFrom,
            String createdBy,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.insuranceCategory = insuranceCategory;
        this.taxType = taxType;
        this.rate = rate;
        this.validFrom = validFrom;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}