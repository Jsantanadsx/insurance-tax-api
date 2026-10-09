package com.insurance.tax.application.port.out;

import com.insurance.tax.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Define as operacoes de persistencia utilizadas
 * pela aplicacao para trabalhar com produtos.
 *
 * A camada de aplicacao conhece apenas este contrato,
 * sem depender diretamente de JPA ou do banco de dados.
 */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAll();
}