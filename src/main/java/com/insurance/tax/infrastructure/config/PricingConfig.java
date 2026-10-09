package com.insurance.tax.infrastructure.config;

import com.insurance.tax.domain.pricing.PricingCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura os componentes de calculo utilizados
 * pela aplicacao.
 */
@Configuration
public class PricingConfig {

    @Bean
    public PricingCalculator pricingCalculator() {
        return new PricingCalculator();
    }
}