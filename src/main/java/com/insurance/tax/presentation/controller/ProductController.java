package com.insurance.tax.presentation.controller;

import com.insurance.tax.application.service.ProductService;
import com.insurance.tax.domain.model.Product;
import com.insurance.tax.presentation.dto.request.ProductRequest;
import com.insurance.tax.presentation.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse criarProduto(
            @RequestBody ProductRequest request
    ) {

        Product product =
                productService.createProduct(
                        request.nome(),
                        request.categoria(),
                        request.precoBase()
                );

        return ProductResponse.from(product);
    }
}