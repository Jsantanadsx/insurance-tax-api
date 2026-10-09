package com.insurance.tax.infrastructure.persistence.mapper;

import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import com.insurance.tax.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    @Test
    void shouldConvertProductToEntity() {

        UUID id = UUID.randomUUID();

        Product product = new Product(
                id,
                "Seguro de Vida Individual",
                InsuranceCategory.VIDA,
                new BigDecimal("100.00"),
                new BigDecimal("103.20")
        );

        ProductEntity entity =
                ProductMapper.toEntity(product);

        assertEquals(id, entity.getId());
        assertEquals(
                "Seguro de Vida Individual",
                entity.getName()
        );
        assertEquals(
                InsuranceCategory.VIDA,
                entity.getCategory()
        );
        assertEquals(
                new BigDecimal("100.00"),
                entity.getBasePrice()
        );
        assertEquals(
                new BigDecimal("103.20"),
                entity.getTariffedPrice()
        );
    }

    @Test
    void shouldConvertEntityToProduct() {

        UUID id = UUID.randomUUID();

        ProductEntity entity =
                new ProductEntity(
                        id,
                        "Seguro Auto",
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                );

        Product product =
                ProductMapper.toDomain(entity);

        assertEquals(id, product.id());
        assertEquals(
                "Seguro Auto",
                product.name()
        );
        assertEquals(
                InsuranceCategory.AUTO,
                product.category()
        );
        assertEquals(
                new BigDecimal("50.00"),
                product.basePrice()
        );
        assertEquals(
                new BigDecimal("55.25"),
                product.tariffedPrice()
        );
    }
}