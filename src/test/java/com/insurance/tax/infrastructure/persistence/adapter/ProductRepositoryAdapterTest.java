package com.insurance.tax.infrastructure.persistence.adapter;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import com.insurance.tax.infrastructure.persistence.entity.ProductEntity;
import com.insurance.tax.infrastructure.persistence.repository.InsuranceProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private InsuranceProductJpaRepository jpaRepository;

    private ProductRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter =
                new ProductRepositoryAdapter(jpaRepository);
    }

    @Test
    void shouldSaveProduct() {

        UUID id = UUID.randomUUID();

        Product product =
                new Product(
                        id,
                        "Seguro de Vida Individual",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        ProductEntity savedEntity =
                new ProductEntity(
                        id,
                        "Seguro de Vida Individual",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        when(
                jpaRepository.save(any(ProductEntity.class))
        ).thenReturn(savedEntity);

        Product result =
                adapter.save(product);

        assertEquals(id, result.id());
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

        verify(jpaRepository)
                .save(any(ProductEntity.class));
    }

    @Test
    void shouldFindProductById() {

        UUID id = UUID.randomUUID();

        ProductEntity entity =
                new ProductEntity(
                        id,
                        "Seguro Auto",
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                );

        when(
                jpaRepository.findById(id)
        ).thenReturn(Optional.of(entity));

        Optional<Product> result =
                adapter.findById(id);

        assertTrue(result.isPresent());

        assertEquals(
                "Seguro Auto",
                result.get().name()
        );

        assertEquals(
                new BigDecimal("55.25"),
                result.get().tariffedPrice()
        );

        verify(jpaRepository)
                .findById(id);
    }

    @Test
    void shouldReturnEmptyWhenProductDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(
                jpaRepository.findById(id)
        ).thenReturn(Optional.empty());

        Optional<Product> result =
                adapter.findById(id);

        assertTrue(result.isEmpty());

        verify(jpaRepository)
                .findById(id);
    }

    @Test
    void shouldFindAllProducts() {

        ProductEntity firstEntity =
                new ProductEntity(
                        UUID.randomUUID(),
                        "Seguro de Vida",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                );

        ProductEntity secondEntity =
                new ProductEntity(
                        UUID.randomUUID(),
                        "Seguro Auto",
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                );

        when(
                jpaRepository.findAll()
        ).thenReturn(
                List.of(
                        firstEntity,
                        secondEntity
                )
        );

        List<Product> result =
                adapter.findAll();

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

        verify(jpaRepository)
                .findAll();
    }
}