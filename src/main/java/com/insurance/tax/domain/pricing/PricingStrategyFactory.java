package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public final class PricingStrategyFactory {

    private final Map<InsuranceCategory, PricingStrategy> strategies;

    public PricingStrategyFactory(
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
