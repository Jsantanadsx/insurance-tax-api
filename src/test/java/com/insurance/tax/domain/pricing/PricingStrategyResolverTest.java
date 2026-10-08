package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a escolha da estratégia de cálculo de acordo
 * com a categoria do seguro.
 *
 * Também são testadas situações inválidas, como estratégias
 * duplicadas ou categorias sem uma estratégia disponível.
 */

class PricingStrategyResolverTest {

    @Test
    void shouldReturnCorrectStrategyForCategory() {

        PricingStrategyResolver factory =
                new PricingStrategyResolver(
                        List.of(
                                new LifePricingStrategy(),
                                new AutoPricingStrategy(),
                                new TravelPricingStrategy(),
                                new ResidentialPricingStrategy(),
                                new PatrimonialPricingStrategy()
                        )
                );

        PricingStrategy strategy =
                factory.getStrategy(InsuranceCategory.VIDA);

        assertInstanceOf(
                LifePricingStrategy.class,
                strategy
        );
    }

    @Test
    void shouldReturnCorrectStrategyForAllCategories() {

        PricingStrategyResolver factory =
                new PricingStrategyResolver(
                        List.of(
                                new LifePricingStrategy(),
                                new AutoPricingStrategy(),
                                new TravelPricingStrategy(),
                                new ResidentialPricingStrategy(),
                                new PatrimonialPricingStrategy()
                        )
                );

        assertInstanceOf(
                LifePricingStrategy.class,
                factory.getStrategy(InsuranceCategory.VIDA)
        );

        assertInstanceOf(
                AutoPricingStrategy.class,
                factory.getStrategy(InsuranceCategory.AUTO)
        );

        assertInstanceOf(
                TravelPricingStrategy.class,
                factory.getStrategy(InsuranceCategory.VIAGEM)
        );

        assertInstanceOf(
                ResidentialPricingStrategy.class,
                factory.getStrategy(InsuranceCategory.RESIDENCIAL)
        );

        assertInstanceOf(
                PatrimonialPricingStrategy.class,
                factory.getStrategy(InsuranceCategory.PATRIMONIAL)
        );
    }

    @Test
    void shouldRejectNullCategory() {

        PricingStrategyResolver factory =
                new PricingStrategyResolver(
                        List.of(
                                new LifePricingStrategy()
                        )
                );

        assertThrows(
                NullPointerException.class,
                () -> factory.getStrategy(null)
        );
    }

    @Test
    void shouldRejectNullStrategyCollection() {

        assertThrows(
                NullPointerException.class,
                () -> new PricingStrategyResolver(null)
        );
    }

    @Test
    void shouldRejectDuplicatedStrategyForSameCategory() {

        assertThrows(
                IllegalStateException.class,
                () -> new PricingStrategyResolver(
                        List.of(
                                new LifePricingStrategy(),
                                new LifePricingStrategy()
                        )
                )
        );
    }

    @Test
    void shouldRejectCategoryWithoutRegisteredStrategy() {

        PricingStrategyResolver factory =
                new PricingStrategyResolver(
                        List.of(
                                new LifePricingStrategy()
                        )
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> factory.getStrategy(
                        InsuranceCategory.AUTO
                )
        );
    }
}