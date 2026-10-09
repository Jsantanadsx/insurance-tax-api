package com.insurance.tax.domain.model;

/**
 * Representa as categorias de seguro aceitas pela aplicação.
 *
 * Foi utilizado um enum para limitar as categorias aos valores
 * definidos no desafio. Dessa forma, evitamos que valores inválidos,
 * como "VIDAAA", "carro" ou "seguro_auto", sejam utilizados
 * durante o processamento.
 *
 * Os valores permanecem em português porque seguem exatamente
 * as categorias definidas no contrato do desafio.
 */

public enum InsuranceCategory {

    VIDA,
    AUTO,
    VIAGEM,
    RESIDENCIAL,
    PATRIMONIAL

}
