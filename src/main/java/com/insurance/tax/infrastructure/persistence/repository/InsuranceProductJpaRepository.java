package com.insurance.tax.infrastructure.persistence.repository;

import com.insurance.tax.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InsuranceProductJpaRepository
        extends JpaRepository<ProductEntity, UUID> {
}