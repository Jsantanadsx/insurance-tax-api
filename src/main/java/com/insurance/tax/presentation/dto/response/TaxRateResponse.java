package com.insurance.tax.presentation.dto.response;

import com.insurance.tax.domain.pricing.TaxRateVersion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa uma taxa retornada pela API administrativa.
 *
 * <p>Os campos são apresentados em português porque fazem
 * parte do contrato externo utilizado pelo operador.</p>
 */
public record TaxRateResponse(
        UUID id,
        String categoriaSeguro,
        String tipoImposto,
        BigDecimal taxa,
        LocalDateTime vigenteDesde,
        String criadoPor,
        LocalDateTime criadoEm
) {

    public static TaxRateResponse from(
            TaxRateVersion taxRateVersion
    ) {
        return new TaxRateResponse(
                taxRateVersion.id(),
                taxRateVersion.insuranceCategory().name(),
                taxRateVersion.taxType().name(),
                taxRateVersion.rate(),
                taxRateVersion.validFrom(),
                taxRateVersion.createdBy(),
                taxRateVersion.createdAt()
        );
    }
}