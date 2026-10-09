package com.insurance.tax.presentation.controller;

import com.insurance.tax.application.service.ProductService;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldCreateProductAndIgnoreTariffedPriceFromRequest()
            throws Exception {

        Product createdProduct =
                new Product(
                        UUID.randomUUID(),
                        "Seguro de Vida Individual",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        when(
                productService.createProduct(
                        "Seguro de Vida Individual",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00")
                )
        ).thenReturn(createdProduct);

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "nome": "Seguro de Vida Individual",
                                          "categoria": "VIDA",
                                          "preco_base": 100.00,
                                          "preco_tarifado": 999999.99
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.nome")
                                .value("Seguro de Vida Individual")
                )
                .andExpect(
                        jsonPath("$.categoria")
                                .value("VIDA")
                )
                .andExpect(
                        jsonPath("$.preco_base")
                                .value(100.00)
                )
                .andExpect(
                        jsonPath("$.preco_tarifado")
                                .value(103.20)
                );

        verify(productService)
                .createProduct(
                        "Seguro de Vida Individual",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00")
                );
    }
}