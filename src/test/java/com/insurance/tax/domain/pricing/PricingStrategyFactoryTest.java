package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PricingStrategyFactoryTest {

    @Test
    void shouldReturnCorrectStrategyForCategory() {

        PricingStrategyFactory factory =
                new PricingStrategyFactory(
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

        PricingStrategyFactory factory =
                new PricingStrategyFactory(
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

        PricingStrategyFactory factory =
                new PricingStrategyFactory(
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
                () -> new PricingStrategyFactory(null)
        );
    }

    @Test
    void shouldRejectDuplicatedStrategyForSameCategory() {

        assertThrows(
                IllegalStateException.class,
                () -> new PricingStrategyFactory(
                        List.of(
                                new LifePricingStrategy(),
                                new LifePricingStrategy()
                        )
                )
        );
    }

    @Test
    void shouldRejectCategoryWithoutRegisteredStrategy() {

        PricingStrategyFactory factory =
                new PricingStrategyFactory(
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