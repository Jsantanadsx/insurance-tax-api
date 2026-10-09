package com.insurance.tax.application.exception;

import java.util.UUID;

/**
 * Indica que o produto informado não foi encontrado.
 */
public class ProductNotFoundException
        extends RuntimeException {

    public ProductNotFoundException(UUID id) {
        super("Product not found: " + id);
    }
}