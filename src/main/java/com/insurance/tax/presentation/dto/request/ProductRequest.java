package com.insurance.tax.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.insurance.tax.domain.model.InsuranceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Representa os dados recebidos pela API
 * para criação ou alteração de um produto.
 *
 * O preço tarifado não é recebido como parte
 * da regra de negócio, pois deve ser calculado
 * pela própria aplicação.
 *
 * Campos adicionais enviados pelo cliente são
 * ignorados. Dessa forma, mesmo que preco_tarifado
 * seja informado, ele não será utilizado.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductRequest(

        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotNull(message = "categoria é obrigatória")
        InsuranceCategory categoria,

        @NotNull(message = "preco_base é obrigatório")
        @Positive(message = "preco_base deve ser maior que zero")
        @JsonProperty("preco_base")
        BigDecimal precoBase

) {
}