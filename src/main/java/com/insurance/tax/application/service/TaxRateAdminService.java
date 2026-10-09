package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.TaxRateRepository;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Reune as operacoes administrativas relacionadas
 * as taxas utilizadas pela aplicacao.
 *
 * Este servico permite consultar as taxas vigentes
 * sem expor detalhes de persistencia para a camada HTTP.
 */
@Service
@RequiredArgsConstructor
public class TaxRateAdminService {

    private final TaxRateRepository taxRateRepository;

    public List<TaxRateVersion> findApplicableRates(
            LocalDateTime referenceDate
    ) {

        Objects.requireNonNull(
                referenceDate,
                "Reference date must not be null"
        );

        return taxRateRepository
                .findApplicableRates(referenceDate);
    }
}