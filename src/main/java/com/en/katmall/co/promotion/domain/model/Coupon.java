/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.promotion.domain.model;

import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.enums.KTypeDiscountType;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Coupon aggregate root for promotional discount management.
 * Handles coupon validation, usage tracking, and discount calculations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class Coupon extends AggregateRoot<String> {

    // Error codes
    private static final String ERR_INVALID_DISCOUNT = "INVALID_DISCOUNT";
    private static final String ERR_COUPON_EXPIRED = "COUPON_EXPIRED";
    private static final String ERR_COUPON_NOT_STARTED = "COUPON_NOT_STARTED";
    private static final String ERR_COUPON_INACTIVE = "COUPON_INACTIVE";
    private static final String ERR_USAGE_LIMIT_EXCEEDED = "USAGE_LIMIT_EXCEEDED";
    private static final String ERR_MIN_ORDER_NOT_MET = "MIN_ORDER_NOT_MET";

    private String code;
    private String description;
    private KTypeDiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderValue;
    private Integer maxUsageLimit;
    private int usageCount;
    private Instant startDate;
    private Instant endDate;
    private boolean isActive;

    /** Default constructor for JPA */
    protected Coupon() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private Coupon(Builder builder) {
        super(builder.code); // Using code as ID for coupons
        this.code = Objects.requireNonNull(builder.code, "code must not be null").toUpperCase();
        this.description = builder.description;
        this.discountType = Objects.requireNonNull(builder.discountType, "discountType must not be null");
        this.discountValue = validateDiscountValue(builder.discountValue, builder.discountType);
        this.maxDiscountAmount = builder.maxDiscountAmount;
        this.minOrderValue = builder.minOrderValue != null ? builder.minOrderValue : BigDecimal.ZERO;
        this.maxUsageLimit = builder.maxUsageLimit;
        this.usageCount = 0;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.isActive = builder.isActive;
    }

    /**
     * Creates a new builder for Coupon
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Validates discount value based on type
     */
    private BigDecimal validateDiscountValue(BigDecimal value, KTypeDiscountType type) {
        Objects.requireNonNull(value, "discountValue must not be null");
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(ERR_INVALID_DISCOUNT, "Discount value must be positive");
        }
        if (type == KTypeDiscountType.PERCENTAGE && value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new DomainException(ERR_INVALID_DISCOUNT, "Percentage discount cannot exceed 100%");
        }
        return value;
    }

    /**
     * Validates if coupon can be applied to an order
     * 
     * @param orderValue The order subtotal
     * @throws DomainException if coupon cannot be applied
     */
    public void validate(BigDecimal orderValue) {
        if (!isActive) {
            throw new DomainException(ERR_COUPON_INACTIVE, "Coupon is not active");
        }

        Instant now = Instant.now();
        if (startDate != null && now.isBefore(startDate)) {
            throw new DomainException(ERR_COUPON_NOT_STARTED, "Coupon is not yet valid");
        }
        if (endDate != null && now.isAfter(endDate)) {
            throw new DomainException(ERR_COUPON_EXPIRED, "Coupon has expired");
        }

        if (maxUsageLimit != null && usageCount >= maxUsageLimit) {
            throw new DomainException(ERR_USAGE_LIMIT_EXCEEDED, "Coupon usage limit exceeded");
        }

        if (orderValue.compareTo(minOrderValue) < 0) {
            throw new DomainException(ERR_MIN_ORDER_NOT_MET,
                    String.format("Minimum order value of %s required", minOrderValue));
        }
    }

    /**
     * Calculates the discount amount for an order
     * 
     * @param orderValue The order subtotal
     * @return The discount amount to apply
     */
    public BigDecimal calculateDiscount(BigDecimal orderValue) {
        validate(orderValue);

        BigDecimal discount;
        if (discountType == KTypeDiscountType.PERCENTAGE) {
            discount = orderValue.multiply(discountValue).divide(BigDecimal.valueOf(100));
            if (maxDiscountAmount != null && discount.compareTo(maxDiscountAmount) > 0) {
                discount = maxDiscountAmount;
            }
        } else {
            discount = discountValue;
        }

        // Discount cannot exceed order value
        if (discount.compareTo(orderValue) > 0) {
            discount = orderValue;
        }

        return discount;
    }

    /**
     * Records a usage of this coupon
     */
    public void recordUsage() {
        this.usageCount++;
        markAsUpdated();
    }

    /**
     * Reverts a usage (e.g., order cancelled)
     */
    public void revertUsage() {
        if (this.usageCount > 0) {
            this.usageCount--;
            markAsUpdated();
        }
    }

    /**
     * Activates the coupon
     */
    public void activate() {
        this.isActive = true;
        markAsUpdated();
    }

    /**
     * Deactivates the coupon
     */
    public void deactivate() {
        this.isActive = false;
        markAsUpdated();
    }

    /**
     * Updates coupon validity period
     * 
     * @param startDate New start date
     * @param endDate   New end date
     */
    public void updateValidityPeriod(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        markAsUpdated();
    }

    /**
     * Updates usage limit
     * 
     * @param limit New maximum usage limit (null for unlimited)
     */
    public void updateUsageLimit(Integer limit) {
        this.maxUsageLimit = limit;
        markAsUpdated();
    }

    /**
     * Checks if coupon is currently valid
     * 
     * @return true if coupon can be used
     */
    public boolean isValid() {
        if (!isActive)
            return false;

        Instant now = Instant.now();
        if (startDate != null && now.isBefore(startDate))
            return false;
        if (endDate != null && now.isAfter(endDate))
            return false;
        if (maxUsageLimit != null && usageCount >= maxUsageLimit)
            return false;

        return true;
    }

    /**
     * Gets remaining usage count
     * 
     * @return Remaining uses, or null if unlimited
     */
    public Integer getRemainingUsage() {
        if (maxUsageLimit == null)
            return null;
        return Math.max(0, maxUsageLimit - usageCount);
    }

    /**
     * Builder class for Coupon
     */
    public static class Builder {
        private String code;
        private String description;
        private KTypeDiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal maxDiscountAmount;
        private BigDecimal minOrderValue;
        private Integer maxUsageLimit;
        private Instant startDate;
        private Instant endDate;
        private boolean isActive = true;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder discountType(KTypeDiscountType discountType) {
            this.discountType = discountType;
            return this;
        }

        public Builder discountValue(BigDecimal discountValue) {
            this.discountValue = discountValue;
            return this;
        }

        public Builder maxDiscountAmount(BigDecimal maxDiscountAmount) {
            this.maxDiscountAmount = maxDiscountAmount;
            return this;
        }

        public Builder minOrderValue(BigDecimal minOrderValue) {
            this.minOrderValue = minOrderValue;
            return this;
        }

        public Builder maxUsageLimit(Integer maxUsageLimit) {
            this.maxUsageLimit = maxUsageLimit;
            return this;
        }

        public Builder startDate(Instant startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(Instant endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Builds the Coupon instance
         * 
         * @return New Coupon instance
         * @throws NullPointerException if required fields are missing
         */
        public Coupon build() {
            return new Coupon(this);
        }
    }
}
