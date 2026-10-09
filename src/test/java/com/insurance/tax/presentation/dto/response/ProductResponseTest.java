package com.insurance.tax.presentation.dto.response;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductResponseTest {

    @Test
    void shouldConvertProductToResponse() {

        UUID id = UUID.randomUUID();

        Product product =
                new Product(
                        id,
                        "Seguro de Vida Individual",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        ProductResponse response =
                ProductResponse.from(product);

        assertEquals(id, response.id());
        assertEquals(
                "Seguro de Vida Individual",
                response.nome()
        );
        assertEquals(
                "VIDA",
                response.categoria()
        );
        assertEquals(
                new BigDecimal("100.00"),
                response.precoBase()
        );
        assertEquals(
                new BigDecimal("103.20"),
                response.precoTarifado()
        );
    }
}