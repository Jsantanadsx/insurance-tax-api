package com.insurance.tax.presentation.controller;

import com.insurance.tax.application.service.TaxRateAdminService;
import com.insurance.tax.presentation.dto.response.TaxRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/taxas")
@RequiredArgsConstructor
public class TaxRateAdminController {

    private final TaxRateAdminService taxRateAdminService;

    @GetMapping
    public List<TaxRateResponse> listarTaxasVigentes(
            @RequestParam(
                    required = false,
                    name = "dataReferencia"
            )
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataReferencia
    ) {

        LocalDateTime referencia =
                dataReferencia != null
                        ? dataReferencia
                        : LocalDateTime.now();

        return taxRateAdminService
                .findApplicableRates(referencia)
                .stream()
                .map(TaxRateResponse::from)
                .toList();
    }
}