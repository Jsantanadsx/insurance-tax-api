package com.insurance.tax.infrastructure.persistence.mapper;

import com.insurance.tax.domain.model.Product;
import com.insurance.tax.infrastructure.persistence.entity.ProductEntity;

/**
 * Responsavel por converter produtos entre
 * o modelo de dominio e o modelo de persistencia.
 *
 * Dessa forma, o dominio nao precisa conhecer
 * as classes utilizadas pelo JPA.
 */
public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.id(),
                product.name(),
                product.category(),
                product.basePrice(),
                product.tariffedPrice()
        );
    }

    public static Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getBasePrice(),
                entity.getTariffedPrice()
        );
    }
}