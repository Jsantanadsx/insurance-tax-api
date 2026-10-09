package com.insurance.tax.infrastructure.persistence.adapter;

import com.insurance.tax.application.port.out.ProductRepository;
import com.insurance.tax.domain.model.Product;
import com.insurance.tax.infrastructure.persistence.entity.ProductEntity;
import com.insurance.tax.infrastructure.persistence.mapper.ProductMapper;
import com.insurance.tax.infrastructure.persistence.repository.InsuranceProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementa o acesso aos produtos utilizando
 * Spring Data JPA.
 *
 * <p>A aplicacao trabalha apenas com Product,
 * enquanto os detalhes de persistencia ficam
 * isolados nesta camada.</p>
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter
        implements ProductRepository {

    private final InsuranceProductJpaRepository jpaRepository;

    @Override
    public Product save(Product product) {

        ProductEntity entity =
                ProductMapper.toEntity(product);

        ProductEntity savedEntity =
                jpaRepository.save(entity);

        return ProductMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(UUID id) {

        return jpaRepository
                .findById(id)
                .map(ProductMapper::toDomain);
    }
}