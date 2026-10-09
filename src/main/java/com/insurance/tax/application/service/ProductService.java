package com.insurance.tax.application.service;

import com.insurance.tax.application.port.out.ProductRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Coordena as operacoes relacionadas aos produtos de seguro.
 *
 * <p>Na criacao de um produto, o preco tarifado nao e recebido
 * pronto. Ele e calculado pela aplicacao com base na categoria,
 * no preco base e nas taxas vigentes.</p>
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
}