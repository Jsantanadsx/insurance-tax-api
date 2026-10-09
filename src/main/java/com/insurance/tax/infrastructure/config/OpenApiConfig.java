package com.insurance.tax.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI insuranceTaxOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Insurance Tax API")
                                .description(
                                        "API para cadastro e atualização de produtos de seguro "
                                                + "com cálculo automático do preço tarifado."
                                )
                                .version("1.0.0")
                );
    }
}