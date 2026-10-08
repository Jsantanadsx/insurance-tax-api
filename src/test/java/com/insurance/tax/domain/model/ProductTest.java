package com.insurance.tax.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateValidProduct() {
        UUID id = UUID.randomUUID();

        Product product = new Product(
                id,
                "Seguro de Vida Individual",
                InsuranceCategory.VIDA,
                new BigDecimal("100.00"),
                new BigDecimal("103.20")
        );

        assertEquals(id, product.id());
        assertEquals("Seguro de Vida Individual", product.name());
        assertEquals(InsuranceCategory.VIDA, product.category());
        assertEquals(new BigDecimal("100.00"), product.basePrice());
        assertEquals(new BigDecimal("103.20"), product.tariffedPrice());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(
                NullPointerException.class,
                () -> new Product(
                        null,
                        "Seguro Auto",
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                )
        );
    }

    @Test
    void shouldRejectNullName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        null,
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                )
        );
    }

    @Test
    void shouldRejectBlankName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "   ",
                        InsuranceCategory.AUTO,
                        new BigDecimal("50.00"),
                        new BigDecimal("55.25")
                )
        );
    }

    @Test
    void shouldRejectNullCategory() {
        assertThrows(
                NullPointerException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "Seguro",
                        null,
                        new BigDecimal("100.00"),
                        new BigDecimal("103.20")
                )
        );
    }

    @Test
    void shouldRejectNullBasePrice() {
        assertThrows(
                NullPointerException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "Seguro",
                        InsuranceCategory.VIDA,
                        null,
                        new BigDecimal("103.20")
                )
        );
    }

    @Test
    void shouldRejectZeroBasePrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "Seguro",
                        InsuranceCategory.VIDA,
                        BigDecimal.ZERO,
                        new BigDecimal("103.20")
                )
        );
    }

    @Test
    void shouldRejectNegativeBasePrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "Seguro",
                        InsuranceCategory.VIDA,
                        new BigDecimal("-10.00"),
                        new BigDecimal("103.20")
                )
        );
    }

    @Test
    void shouldRejectNullTariffedPrice() {
        assertThrows(
                NullPointerException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "Seguro",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        null
                )
        );
    }

    @Test
    void shouldRejectZeroTariffedPrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "Seguro",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldRejectNegativeTariffedPrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(
                        UUID.randomUUID(),
                        "Seguro",
                        InsuranceCategory.VIDA,
                        new BigDecimal("100.00"),
                        new BigDecimal("-1.00")
                )
        );
    }
}