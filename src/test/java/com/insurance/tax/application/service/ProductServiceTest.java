package com.insurance.tax.application.service;

import com.insurance.tax.application.exception.ProductNotFoundException;
import com.insurance.tax.application.port.out.ProductRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    void shouldUpdateExistingProductAndRecalculatePrice() {

        UUID id = UUID.randomUUID();

        Product existingProduct =
                new Product(
                        id,
                        "Seguro de Vida",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        when(
                productRepository.findById(id)
        ).thenReturn(
                Optional.of(existingProduct)
        );

        when(
                insurancePricingService.calculatePrice(
                        eq(InsuranceCategory.VIDA),
                        eq(new BigDecimal("200.00")),
                        any()
                )
        ).thenReturn(
                new BigDecimal("206.40")
        );

        when(
                productRepository.save(any(Product.class))
        ).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        Product result =
                productService.updateProduct(
                        id,
                        "Seguro de Vida Premium",
                        InsuranceCategory.VIDA,
                        new BigDecimal("200.00")
                );

        assertEquals(
                id,
                result.id()
        );

        assertEquals(
                "Seguro de Vida Premium",
                result.name()
        );

        assertEquals(
                InsuranceCategory.VIDA,
                result.category()
        );

        assertEquals(
                new BigDecimal("200.00"),
                result.basePrice()
        );

        assertEquals(
                new BigDecimal("206.40"),
                result.tariffedPrice()
        );

        verify(productRepository)
                .findById(id);

        verify(insurancePricingService)
                .calculatePrice(
                        eq(InsuranceCategory.VIDA),
                        eq(new BigDecimal("200.00")),
                        any()
                );

        verify(productRepository)
                .save(any(Product.class));
    }

    @Test
    void shouldRejectUpdateWhenProductDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(
                productRepository.findById(id)
        ).thenReturn(
                Optional.empty()
        );

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(
                        id,
                        "Seguro inexistente",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00")
                )
        );

        verify(productRepository)
                .findById(id);

        verifyNoInteractions(
                insurancePricingService
        );

        verify(productRepository, never())
                .save(any(Product.class));
    }

    @Test
    void shouldListProducts() {

        Product firstProduct =
                new Product(
                        UUID.randomUUID(),
                        "Seguro de Vida",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        Product secondProduct =
                new Product(
                        UUID.randomUUID(),
                        "Seguro Auto",
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                );

        when(
                productRepository.findAll()
        ).thenReturn(
                List.of(
                        firstProduct,
                        secondProduct
                )
        );

        List<Product> result =
                productService.listProducts();

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                "Seguro de Vida",
                result.get(0).name()
        );

        assertEquals(
                "Seguro Auto",
                result.get(1).name()
        );

        verify(productRepository)
                .findAll();

        verifyNoInteractions(
                insurancePricingService
        );
    }
}