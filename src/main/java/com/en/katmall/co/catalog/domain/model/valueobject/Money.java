/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.domain.model.valueobject;

import com.en.katmall.co.shared.domain.ValueObject;
import com.en.katmall.co.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing a monetary amount with currency.
 * Immutable with proper decimal handling for financial calculations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class Money extends ValueObject {

    private final BigDecimal amount;
    private final String currency;

    /**
     * Private constructor - use factory methods
     * 
     * @param amount   The monetary amount
     * @param currency The currency code
     */
    private Money(BigDecimal amount, String currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    /**
     * Creates Money with default currency (VND)
     * 
     * @param amount The monetary amount
     * @return New Money instance
     * @throws ValidationException if amount is invalid
     */
    public static Money of(BigDecimal amount) {
        return of(amount, "VND");
    }

    /**
     * Creates Money with specified currency
     * 
     * @param amount   The monetary amount
     * @param currency The currency code
     * @return New Money instance
     * @throws ValidationException if amount is invalid
     */
    public static Money of(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new ValidationException("amount", "Amount is required");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("amount", "Amount cannot be negative");
        }
        return new Money(amount, currency);
    }

    /**
     * Creates zero Money with default currency
     * 
     * @return Money with zero amount
     */
    public static Money zero() {
        return new Money(BigDecimal.ZERO, "VND");
    }

    /**
     * Adds another Money to this one
     * 
     * @param other The Money to add
     * @return New Money with summed amount
     */
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Subtracts another Money from this one
     * 
     * @param other The Money to subtract
     * @return New Money with difference
     */
    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Multiplies this Money by a quantity
     * 
     * @param quantity The multiplier
     * @return New Money with multiplied amount
     */
    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }

    /**
     * Gets the amount
     * 
     * @return The monetary amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Gets the currency code
     * 
     * @return The currency code
     */
    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
