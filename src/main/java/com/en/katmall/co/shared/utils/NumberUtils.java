/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Optional;

/**
 * Utility class for number operations and conversions.
 * Provides safe number parsing and formatting.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class NumberUtils {

    private static final Locale VN_LOCALE = new Locale("vi", "VN");
    private static final DecimalFormat VN_CURRENCY_FORMAT = new DecimalFormat("#,### ₫");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");

    private NumberUtils() {
        // Private constructor to prevent instantiation
    }


    /**
     * Safely parses string to Integer
     * 
     * @param str The string to parse
     * @return Optional containing the Integer
     */
    public static Optional<Integer> parseInteger(String str) {
        if (ObjectUtils.isBlank(str))
            return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(str.trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses string to int with default value
     * 
     * @param str          The string to parse
     * @param defaultValue Default if parsing fails
     * @return Parsed int or default
     */
    public static int parseIntOrDefault(String str, int defaultValue) {
        return parseInteger(str).orElse(defaultValue);
    }

    /**
     * Safely parses string to Long
     * 
     * @param str The string to parse
     * @return Optional containing the Long
     */
    public static Optional<Long> parseLong(String str) {
        if (ObjectUtils.isBlank(str))
            return Optional.empty();
        try {
            return Optional.of(Long.parseLong(str.trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses string to long with default value
     * 
     * @param str          The string to parse
     * @param defaultValue Default if parsing fails
     * @return Parsed long or default
     */
    public static long parseLongOrDefault(String str, long defaultValue) {
        return parseLong(str).orElse(defaultValue);
    }

    /**
     * Safely parses string to Double
     * 
     * @param str The string to parse
     * @return Optional containing the Double
     */
    public static Optional<Double> parseDouble(String str) {
        if (ObjectUtils.isBlank(str))
            return Optional.empty();
        try {
            return Optional.of(Double.parseDouble(str.trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses string to double with default value
     * 
     * @param str          The string to parse
     * @param defaultValue Default if parsing fails
     * @return Parsed double or default
     */
    public static double parseDoubleOrDefault(String str, double defaultValue) {
        return parseDouble(str).orElse(defaultValue);
    }

    /**
     * Safely parses string to BigDecimal
     * 
     * @param str The string to parse
     * @return Optional containing the BigDecimal
     */
    public static Optional<BigDecimal> parseBigDecimal(String str) {
        if (ObjectUtils.isBlank(str))
            return Optional.empty();
        try {
            return Optional.of(new BigDecimal(str.trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses string to BigDecimal with default value
     * 
     * @param str          The string to parse
     * @param defaultValue Default if parsing fails
     * @return Parsed BigDecimal or default
     */
    public static BigDecimal parseBigDecimalOrDefault(String str, BigDecimal defaultValue) {
        return parseBigDecimal(str).orElse(defaultValue);
    }


    /**
     * Formats number with Vietnamese currency symbol
     * 
     * @param amount The amount to format
     * @return Formatted currency string (e.g., "1,500,000 ₫")
     */
    public static String formatVnCurrency(BigDecimal amount) {
        if (amount == null)
            return "0 ₫";
        return VN_CURRENCY_FORMAT.format(amount);
    }

    /**
     * Formats number with Vietnamese currency symbol
     * 
     * @param amount The amount to format
     * @return Formatted currency string
     */
    public static String formatVnCurrency(long amount) {
        return VN_CURRENCY_FORMAT.format(amount);
    }

    /**
     * Formats number with thousand separators
     * 
     * @param number The number to format
     * @return Formatted number string
     */
    public static String formatNumber(Number number) {
        if (number == null)
            return "0";
        return DECIMAL_FORMAT.format(number);
    }

    /**
     * Formats number as percentage
     * 
     * @param value The value (0-1 range)
     * @return Formatted percentage (e.g., "25%")
     */
    public static String formatPercent(double value) {
        return String.format("%.0f%%", value * 100);
    }

    /**
     * Formats number as percentage with decimals
     * 
     * @param value    The value (0-1 range)
     * @param decimals Number of decimal places
     * @return Formatted percentage
     */
    public static String formatPercent(double value, int decimals) {
        return String.format("%." + decimals + "f%%", value * 100);
    }


    /**
     * Calculates percentage of a value
     * 
     * @param value   The base value
     * @param percent The percentage (0-100)
     * @return The result
     */
    public static BigDecimal percentOf(BigDecimal value, BigDecimal percent) {
        if (value == null || percent == null)
            return BigDecimal.ZERO;
        return value.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * Rounds BigDecimal to specified decimal places
     * 
     * @param value The value to round
     * @param scale Number of decimal places
     * @return Rounded value
     */
    public static BigDecimal round(BigDecimal value, int scale) {
        if (value == null)
            return BigDecimal.ZERO;
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * Safely divides two BigDecimals
     * 
     * @param dividend The dividend
     * @param divisor  The divisor
     * @param scale    Decimal places for result
     * @return Division result or ZERO if divisor is zero
     */
    public static BigDecimal divideSafe(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (dividend == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return dividend.divide(divisor, scale, RoundingMode.HALF_UP);
    }


    /**
     * Checks if value is positive
     * 
     * @param value The value to check
     * @return true if positive
     */
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if value is negative
     * 
     * @param value The value to check
     * @return true if negative
     */
    public static boolean isNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Checks if value is zero
     * 
     * @param value The value to check
     * @return true if zero
     */
    public static boolean isZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Checks if value is in range (inclusive)
     * 
     * @param value The value to check
     * @param min   Minimum value
     * @param max   Maximum value
     * @return true if in range
     */
    public static boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Clamps value to range
     * 
     * @param value The value to clamp
     * @param min   Minimum value
     * @param max   Maximum value
     * @return Clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Returns max of two BigDecimals (null-safe)
     * 
     * @param a First value
     * @param b Second value
     * @return Maximum value
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return a.compareTo(b) >= 0 ? a : b;
    }

    /**
     * Returns min of two BigDecimals (null-safe)
     * 
     * @param a First value
     * @param b Second value
     * @return Minimum value
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return a.compareTo(b) <= 0 ? a : b;
    }
}
