package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.ProductRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InsurancePricingService insurancePricingService;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService =
                new ProductService(
                        productRepository,
                        insurancePricingService
                );
    }

    @Test
    void shouldCreateProductWithCalculatedTariffedPrice() {

        when(
                insurancePricingService.calculatePrice(
                        eq(InsuranceCategory.VIDA),
                        eq(new BigDecimal("100.00")),
                        any()
                )
        ).thenReturn(
                new BigDecimal("103.20")
        );

        when(
                productRepository.save(any(Product.class))
        ).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        Product result =
                productService.createProduct(
                        "Seguro de Vida Individual",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00")
                );

        assertNotNull(result.id());

        assertEquals(
                "Seguro de Vida Individual",
                result.name()
        );

        assertEquals(
                InsuranceCategory.VIDA,
                result.category()
        );

        assertEquals(
                new BigDecimal("100.00"),
                result.basePrice()
        );

        assertEquals(
                new BigDecimal("103.20"),
                result.tariffedPrice()
        );

        verify(insurancePricingService)
                .calculatePrice(
                        eq(InsuranceCategory.VIDA),
                        eq(new BigDecimal("100.00")),
                        any()
                );

        verify(productRepository)
                .save(any(Product.class));
    }

    @Test
    void shouldPersistTheCalculatedPrice() {

        when(
                insurancePricingService.calculatePrice(
                        eq(InsuranceCategory.AUTO),
                        eq(new BigDecimal("50.00")),
                        any()
                )
        ).thenReturn(
                new BigDecimal("55.25")
        );

        when(
                productRepository.save(any(Product.class))
        ).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        productService.createProduct(
                "Seguro Auto",
                InsuranceCategory.AUTO,
                new BigDecimal("50.00")
        );

        ArgumentCaptor<Product> productCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository)
                .save(productCaptor.capture());

        Product savedProduct =
                productCaptor.getValue();

        assertEquals(
                new BigDecimal("55.25"),
                savedProduct.tariffedPrice()
        );
    }
}