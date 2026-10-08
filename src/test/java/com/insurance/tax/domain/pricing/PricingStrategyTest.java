package com.insurance.tax.domain.pricing;

import com.insurance.tax.domain.model.InsuranceCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testa o cálculo realizado para cada categoria de seguro.
 *
 * O objetivo é garantir que cada categoria utilize as taxas
 * definidas no desafio e produza o preço tarifado esperado.
 */

class PricingStrategyTest {

    @ParameterizedTest
    @MethodSource("pricingScenarios")
    void shouldCalculateTariffedPriceForEachCategory(
            PricingStrategy strategy,
            BigDecimal basePrice,
            BigDecimal expectedPrice
    ) {
        BigDecimal result = strategy.calculate(basePrice);

        assertEquals(expectedPrice, result);
    }

    private static Stream<Arguments> pricingScenarios() {
        return Stream.of(
                Arguments.of(
                        new LifePricingStrategy(),
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                ),
                Arguments.of(
                        new AutoPricingStrategy(),
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                ),
                Arguments.of(
                        new TravelPricingStrategy(),
                        new BigDecimal("100.00"),
                        new BigDecimal("107.00")
                ),
                Arguments.of(
                        new ResidentialPricingStrategy(),
                        new BigDecimal("100.00"),
                        new BigDecimal("107.00")
                ),
                Arguments.of(
                        new PatrimonialPricingStrategy(),
                        new BigDecimal("100.00"),
                        new BigDecimal("108.00")
                )
        );
    }

    @Test
    void shouldReturnSupportedCategoryForEachStrategy() {
        assertEquals(
                InsuranceCategory.VIDA,
                new LifePricingStrategy().supportedCategory()
        );

        assertEquals(
                InsuranceCategory.AUTO,
                new AutoPricingStrategy().supportedCategory()
        );

        assertEquals(
                InsuranceCategory.VIAGEM,
                new TravelPricingStrategy().supportedCategory()
        );

        assertEquals(
                InsuranceCategory.RESIDENCIAL,
                new ResidentialPricingStrategy().supportedCategory()
        );

        assertEquals(
                InsuranceCategory.PATRIMONIAL,
                new PatrimonialPricingStrategy().supportedCategory()
        );
    }
}