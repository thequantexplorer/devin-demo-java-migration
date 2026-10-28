package com.acme.billing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {
    @Test void addsSameCurrency() {
        assertEquals(Money.of("3.50", "USD"), Money.of("1.25", "USD").plus(Money.of("2.25", "USD")));
    }

    @Test void rejectsCurrencyMismatch() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("1", "USD").plus(Money.of("1", "EUR")));
    }

    @Test void bankersRounding() {
        assertEquals(Money.of("0.12", "USD"), Money.of("0.125", "USD"));
        assertEquals(Money.of("0.14", "USD"), Money.of("0.135", "USD"));
    }

    @Test void percent() {
        assertEquals(Money.of("7.25", "USD"), Money.of("100", "USD").percent(new BigDecimal("7.25")));
    }
}
