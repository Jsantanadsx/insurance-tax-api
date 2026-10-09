package com.insurance.tax.domain.pricing;

/**
 * Representa os tipos de impostos utilizados no cálculo
 * do preço tarifado dos seguros.
 *
 * O enum limita os valores possíveis aos impostos
 * definidos pela regra de negócio, evitando valores
 * inválidos durante o processamento.
 */
public enum TaxType {

    IOF,
    PIS,
    COFINS
}