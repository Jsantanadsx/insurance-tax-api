package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.ProductRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.insurance.tax.application.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Coordena as operacoes relacionadas aos produtos de seguro.
 *
 * Na criacao de um produto, o preco tarifado nao e recebido
 * pronto. Ele e calculado pela aplicacao com base na categoria,
 * no preco base e nas taxas vigentes.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InsurancePricingService insurancePricingService;

    public Product createProduct(
            String name,
            InsuranceCategory category,
            BigDecimal basePrice
    ) {

        BigDecimal tariffedPrice =
                insurancePricingService.calculatePrice(
                        category,
                        basePrice,
                        LocalDateTime.now()
                );

        Product product =
                new Product(
                        UUID.randomUUID(),
                        name,
                        category,
                        basePrice,
                        tariffedPrice
                );

        return productRepository.save(product);
    }

    public Product updateProduct(
            UUID id,
            String name,
            InsuranceCategory category,
            BigDecimal basePrice
    ) {

        Product existingProduct =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ProductNotFoundException(id)
                        );

        BigDecimal tariffedPrice =
                insurancePricingService.calculatePrice(
                        category,
                        basePrice,
                        LocalDateTime.now()
                );

        Product updatedProduct =
                new Product(
                        existingProduct.id(),
                        name,
                        category,
                        basePrice,
                        tariffedPrice
                );

        return productRepository.save(updatedProduct);
    }
}