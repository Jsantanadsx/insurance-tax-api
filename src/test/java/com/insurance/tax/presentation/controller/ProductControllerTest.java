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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.insurance.tax.application.exception.ProductNotFoundException;

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

    @Test
    void shouldUpdateProductAndReturnRecalculatedPrice()
            throws Exception {

        UUID id = UUID.randomUUID();

        Product updatedProduct =
                new Product(
                        id,
                        "Seguro de Vida Premium",
                        InsuranceCategory.VIDA,
                        new BigDecimal("200.00"),
                        new BigDecimal("206.40")
                );

        when(
                productService.updateProduct(
                        id,
                        "Seguro de Vida Premium",
                        InsuranceCategory.VIDA,
                        new BigDecimal("200.00")
                )
        ).thenReturn(updatedProduct);

        mockMvc.perform(
                        put("/api/produtos/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "nome": "Seguro de Vida Premium",
                                      "categoria": "VIDA",
                                      "preco_base": 200.00,
                                      "preco_tarifado": 999999.99
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(id.toString())
                )
                .andExpect(
                        jsonPath("$.nome")
                                .value("Seguro de Vida Premium")
                )
                .andExpect(
                        jsonPath("$.categoria")
                                .value("VIDA")
                )
                .andExpect(
                        jsonPath("$.preco_base")
                                .value(200.00)
                )
                .andExpect(
                        jsonPath("$.preco_tarifado")
                                .value(206.40)
                );

        verify(productService)
                .updateProduct(
                        id,
                        "Seguro de Vida Premium",
                        InsuranceCategory.VIDA,
                        new BigDecimal("200.00")
                );
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingProduct()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(
                productService.updateProduct(
                        eq(id),
                        eq("Seguro inexistente"),
                        eq(InsuranceCategory.VIDA),
                        eq(new BigDecimal("100.00"))
                )
        ).thenThrow(
                new ProductNotFoundException(id)
        );

        mockMvc.perform(
                        put("/api/produtos/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "nome": "Seguro inexistente",
                                      "categoria": "VIDA",
                                      "preco_base": 100.00
                                    }
                                    """)
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.erro")
                                .value(
                                        "Product not found: " + id
                                )
                );

        verify(productService)
                .updateProduct(
                        id,
                        "Seguro inexistente",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00")
                );
    }
}