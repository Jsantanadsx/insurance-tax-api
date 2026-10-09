package com.insurance.tax.presentation.controller;

import com.insurance.tax.infrastructure.persistence.repository.InsuranceProductJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.infrastructure.persistence.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InsuranceProductJpaRepository productRepository;

    @Test
    void shouldCreateCalculateAndPersistProduct()
            throws Exception {

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "nome": "Seguro de Vida Individual",
                                          "categoria": "VIDA",
                                          "preco_base": 100.00
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

        assertEquals(
                1,
                productRepository.count()
        );

        var savedProduct =
                productRepository.findAll().get(0);

        assertEquals(
                "Seguro de Vida Individual",
                savedProduct.getName()
        );

        assertEquals(
                0,
                savedProduct.getBasePrice()
                        .compareTo(
                                new java.math.BigDecimal("100.00")
                        )
        );

        assertEquals(
                0,
                savedProduct.getTariffedPrice()
                        .compareTo(
                                new java.math.BigDecimal("103.20")
                        )
        );
    }

    @Test
    void shouldIgnoreTariffedPriceSentByClient()
            throws Exception {

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
                        jsonPath("$.preco_tarifado")
                                .value(103.20)
                );

        assertEquals(
                1,
                productRepository.count()
        );

        var savedProduct =
                productRepository.findAll().get(0);

        assertEquals(
                0,
                savedProduct.getTariffedPrice()
                        .compareTo(
                                new java.math.BigDecimal("103.20")
                        )
        );

        assertTrue(
                savedProduct.getTariffedPrice()
                        .compareTo(
                                new java.math.BigDecimal("999999.99")
                        ) != 0
        );
    }

    @Test
    void shouldUpdateExistingProductWithoutCreatingAnotherRecord()
            throws Exception {

        UUID id = UUID.randomUUID();

        ProductEntity existingProduct =
                new ProductEntity(
                        id,
                        "Seguro de Vida",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        productRepository.saveAndFlush(existingProduct);

        assertEquals(
                1,
                productRepository.count()
        );

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
                        jsonPath("$.preco_base")
                                .value(200.00)
                )
                .andExpect(
                        jsonPath("$.preco_tarifado")
                                .value(206.40)
                );

        assertEquals(
                1,
                productRepository.count()
        );

        ProductEntity updatedProduct =
                productRepository
                        .findById(id)
                        .orElseThrow();

        assertEquals(
                id,
                updatedProduct.getId()
        );

        assertEquals(
                "Seguro de Vida Premium",
                updatedProduct.getName()
        );

        assertEquals(
                0,
                updatedProduct.getBasePrice()
                        .compareTo(
                                new BigDecimal("200.00")
                        )
        );

        assertEquals(
                0,
                updatedProduct.getTariffedPrice()
                        .compareTo(
                                new BigDecimal("206.40")
                        )
        );
    }
}