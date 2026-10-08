package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.math.BigDecimal;

/**
 * Define como uma categoria de seguro deve informar suas taxas
 * e calcular o preço tarifado.
 *
 * Cada categoria possui suas próprias taxas, mas todas utilizam
 * a mesma fórmula de cálculo.
 *
 * Essa estrutura evita concentrar todas as categorias em vários
 * blocos de if ou switch e facilita a inclusão de novas categorias
 * no futuro.
 */

public interface PricingStrategy {

    InsuranceCategory supportedCategory();

    TaxRates taxRates();

    default BigDecimal calculate(BigDecimal basePrice) {
        return taxRates().applyTo(basePrice);
    }
}
