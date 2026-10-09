package com.insurance.tax.infrastructure.persistence.adapter;

import com.insurance.tax.application.port.out.ProductRepository;
import com.insurance.tax.domain.model.Product;
import com.insurance.tax.infrastructure.persistence.entity.ProductEntity;
import com.insurance.tax.infrastructure.persistence.mapper.ProductMapper;
import com.insurance.tax.infrastructure.persistence.repository.InsuranceProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<Product> findAll() {

        return jpaRepository
                .findAll()
                .stream()
                .map(ProductMapper::toDomain)
                .toList();
    }
}