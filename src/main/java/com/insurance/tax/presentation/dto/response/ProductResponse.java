package com.insurance.tax.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.insurance.tax.domain.model.Product;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representa um produto devolvido pela API.
 *
 * Os nomes dos campos seguem o contrato
 * apresentado no desafio.
 */
public record ProductResponse(

        UUID id,

        String nome,

        String categoria,

        @JsonProperty("preco_base")
        BigDecimal precoBase,

        @JsonProperty("preco_tarifado")
        BigDecimal precoTarifado

) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.category().name(),
                product.basePrice(),
                product.tariffedPrice()
        );
    }
}