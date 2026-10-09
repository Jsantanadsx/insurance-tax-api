package com.insurance.tax.application.service;

import com.insurance.tax.application.exception.ProductNotFoundException;
import com.insurance.tax.application.port.out.ProductRepository;
import com.insurance.tax.domain.model.InsuranceCategory;
import com.insurance.tax.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
@Slf4j
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

        log.info(
                "Criando produto de seguro. categoria={}, precoBase={}",
                category,
                basePrice
        );

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

        Product savedProduct =
                productRepository.save(product);

        log.info(
                "Produto criado com sucesso. id={}, categoria={}, precoTarifado={}",
                savedProduct.id(),
                savedProduct.category(),
                savedProduct.tariffedPrice()
        );

        return savedProduct;
    }

    public Product updateProduct(
            UUID id,
            String name,
            InsuranceCategory category,
            BigDecimal basePrice
    ) {

        log.info(
                "Atualizando produto. id={}, categoria={}, precoBase={}",
                id,
                category,
                basePrice
        );

        Product existingProduct =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> {
                                    log.warn(
                                            "Produto nao encontrado para atualizacao. id={}",
                                            id
                                    );

                                    return new ProductNotFoundException(id);
                                }
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

        Product savedProduct =
                productRepository.save(updatedProduct);

        log.info(
                "Produto atualizado com sucesso. id={}, categoria={}, precoTarifado={}",
                savedProduct.id(),
                savedProduct.category(),
                savedProduct.tariffedPrice()
        );

        return savedProduct;
    }

    public List<Product> listProducts() {

        log.debug("Listando produtos de seguro");

        List<Product> products =
                productRepository.findAll();

        log.debug(
                "Produtos de seguro encontrados. total={}",
                products.size()
        );

        return products;
    }
}