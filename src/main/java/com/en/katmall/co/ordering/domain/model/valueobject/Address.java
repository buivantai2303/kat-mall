/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.domain.model.valueobject;

import com.en.katmall.co.shared.domain.ValueObject;

import java.util.Objects;

/**
 * Value Object representing a physical address.
 * Used for shipping and billing addresses in orders.
 * Supports Vietnamese address format with ward/district.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class Address extends ValueObject {

    private final String fullName;
    private final String phoneNumber;
    private final String addressLine1;
    private final String addressLine2;
    private final String city;
    private final String district;
    private final String ward;
    private final String country;
    private final String postalCode;

    /**
     * Private constructor for Builder pattern
     * 
     * @param builder The builder instance
     */
    private Address(Builder builder) {
        this.fullName = builder.fullName;
        this.phoneNumber = builder.phoneNumber;
        this.addressLine1 = builder.addressLine1;
        this.addressLine2 = builder.addressLine2;
        this.city = builder.city;
        this.district = builder.district;
        this.ward = builder.ward;
        this.country = builder.country;
        this.postalCode = builder.postalCode;
    }

    /**
     * Creates a new builder for Address
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getWard() {
        return ward;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets the full formatted address as a single string
     * 
     * @return Formatted full address
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressLine1 != null)
            sb.append(addressLine1);
        if (addressLine2 != null)
            sb.append(", ").append(addressLine2);
        if (ward != null)
            sb.append(", ").append(ward);
        if (district != null)
            sb.append(", ").append(district);
        if (city != null)
            sb.append(", ").append(city);
        if (country != null)
            sb.append(", ").append(country);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return Objects.equals(fullName, address.fullName) &&
                Objects.equals(phoneNumber, address.phoneNumber) &&
                Objects.equals(addressLine1, address.addressLine1) &&
                Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, phoneNumber, addressLine1, city);
    }

    @Override
    public String toString() {
        return getFullAddress();
    }

    /**
     * Builder class for Address
     */
    public static class Builder {
        private String fullName;
        private String phoneNumber;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String district;
        private String ward;
        private String country = "Vietnam";
        private String postalCode;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public Builder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder district(String district) {
            this.district = district;
            return this;
        }

        public Builder ward(String ward) {
            this.ward = ward;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        /**
         * Builds the Address instance
         * 
         * @return New Address instance
         */
        public Address build() {
            return new Address(this);
        }
    }
}
