package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Responsável por encontrar a estratégia de cálculo correta
 * para cada categoria de seguro.
 *
 * Ao receber uma categoria, como VIDA ou AUTO, procura a
 * estratégia responsável por calcular aquele tipo de seguro.
 *
 * Isso permite que as outras partes da aplicação não precisem
 * conhecer diretamente cada implementação de cálculo.
 */

public final class PricingStrategyResolver {

    private final Map<InsuranceCategory, PricingStrategy> strategies;

    public PricingStrategyResolver(
            Collection<PricingStrategy> strategies
    ) {

        Objects.requireNonNull(
                strategies,
                "Pricing strategies must not be null"
        );

        this.strategies = strategies.stream()
                .collect(
                        toMap(
                                PricingStrategy::supportedCategory,
                                Function.identity(),
                                (first, second) -> {
                                    throw new IllegalStateException(
                                            "Duplicate pricing strategy for category: "
                                                    + first.supportedCategory()
                                    );
                                },
                                () -> new EnumMap<>(
                                        InsuranceCategory.class
                                )
                        )
                );
    }

    public PricingStrategy getStrategy(
            InsuranceCategory category
    ) {

        Objects.requireNonNull(
                category,
                "Insurance category must not be null"
        );

        PricingStrategy strategy = strategies.get(category);

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "No pricing strategy found for category: "
                            + category
            );
        }

        return strategy;
    }
}
