package com.insurance.tax.presentation.controller;

import com.insurance.tax.application.service.TaxRateAdminService;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.pricing.TaxRateVersion;
import com.insurance.tax.domain.pricing.TaxType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaxRateAdminController.class)
class TaxRateAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaxRateAdminService taxRateAdminService;

    @Test
    void shouldReturnApplicableRatesInPortugueseContract()
            throws Exception {

        LocalDateTime referenceDate =
                LocalDateTime.of(
                        2026,
                        6,
                        1,
                        0,
                        0
                );

        TaxRateVersion taxRateVersion =
                new TaxRateVersion(
                        UUID.randomUUID(),
                        InsuranceCategory.VIDA,
                        TaxType.IOF,
                        new BigDecimal("0.010000"),
                        LocalDateTime.of(
                                2026,
                                1,
                                1,
                                0,
                                0
                        ),
                        "SYSTEM",
                        LocalDateTime.of(
                                2026,
                                1,
                                1,
                                10,
                                0
                        )
                );

        when(
                taxRateAdminService.findApplicableRates(
                        referenceDate
                )
        ).thenReturn(
                List.of(taxRateVersion)
        );

        mockMvc.perform(
                        get("/api/admin/taxas")
                                .param(
                                        "dataReferencia",
                                        "2026-06-01T00:00:00"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].categoriaSeguro")
                                .value("VIDA")
                )
                .andExpect(
                        jsonPath("$[0].tipoImposto")
                                .value("IOF")
                )
                .andExpect(
                        jsonPath("$[0].taxa")
                                .value(0.01)
                )
                .andExpect(
                        jsonPath("$[0].vigenteDesde")
                                .value(
                                        "2026-01-01T00:00:00"
                                )
                )
                .andExpect(
                        jsonPath("$[0].criadoPor")
                                .value("SYSTEM")
                )
                .andExpect(
                        jsonPath("$[0].criadoEm")
                                .value(
                                        "2026-01-01T10:00:00"
                                )
                );

        verify(taxRateAdminService)
                .findApplicableRates(referenceDate);
    }
}