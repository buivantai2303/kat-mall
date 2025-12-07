/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.utils;

import java.util.regex.Pattern;

/**
 * Utility class for string manipulation and conversion.
 * Provides common string operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class StringUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_VN_PATTERN = Pattern.compile(
            "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$");

    private static final Pattern SLUG_PATTERN = Pattern.compile(
            "^[a-z0-9]+(?:-[a-z0-9]+)*$");

    private StringUtils() {
        // Private constructor to prevent instantiation
    }


    /**
     * Validates email format
     * 
     * @param email The email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (ObjectUtils.isBlank(email))
            return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates Vietnamese phone number format
     * 
     * @param phone The phone number to validate
     * @return true if valid VN phone format
     */
    public static boolean isValidPhoneVN(String phone) {
        if (ObjectUtils.isBlank(phone))
            return false;
        String cleaned = phone.replaceAll("[\\s-]", "");
        return PHONE_VN_PATTERN.matcher(cleaned).matches();
    }

    /**
     * Validates slug format
     * 
     * @param slug The slug to validate
     * @return true if valid slug format
     */
    public static boolean isValidSlug(String slug) {
        if (ObjectUtils.isBlank(slug))
            return false;
        return SLUG_PATTERN.matcher(slug).matches();
    }


    /**
     * Converts string to slug format
     * 
     * @param input The input string
     * @return Slug-formatted string
     */
    public static String toSlug(String input) {
        if (ObjectUtils.isBlank(input))
            return "";
        return input.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * Truncates string to max length with ellipsis
     * 
     * @param str       The string to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (ObjectUtils.isBlank(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Truncates string to max length without ellipsis
     * 
     * @param str       The string to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    public static String truncateStrict(String str, int maxLength) {
        if (ObjectUtils.isBlank(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength);
    }

    /**
     * Capitalizes first letter of string
     * 
     * @param str The string to capitalize
     * @return Capitalized string
     */
    public static String capitalize(String str) {
        if (ObjectUtils.isBlank(str))
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Capitalizes first letter of each word
     * 
     * @param str The string to capitalize
     * @return Title case string
     */
    public static String toTitleCase(String str) {
        if (ObjectUtils.isBlank(str))
            return str;
        String[] words = str.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    /**
     * Converts to camelCase
     * 
     * @param str The string to convert
     * @return camelCase string
     */
    public static String toCamelCase(String str) {
        if (ObjectUtils.isBlank(str))
            return str;
        String[] parts = str.toLowerCase().split("[\\s_-]+");
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                result.append(Character.toUpperCase(parts[i].charAt(0)))
                        .append(parts[i].substring(1));
            }
        }
        return result.toString();
    }

    /**
     * Converts to snake_case
     * 
     * @param str The string to convert
     * @return snake_case string
     */
    public static String toSnakeCase(String str) {
        if (ObjectUtils.isBlank(str))
            return str;
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replaceAll("[\\s-]+", "_")
                .toLowerCase();
    }


    /**
     * Masks email for privacy (jo***@example.com)
     * 
     * @param email The email to mask
     * @return Masked email
     */
    public static String maskEmail(String email) {
        if (!isValidEmail(email))
            return email;
        int atIndex = email.indexOf('@');
        if (atIndex <= 2)
            return email;
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        return localPart.substring(0, 2) + "***" + domain;
    }

    /**
     * Masks phone number for privacy (0912***789)
     * 
     * @param phone The phone to mask
     * @return Masked phone
     */
    public static String maskPhone(String phone) {
        if (ObjectUtils.isBlank(phone) || phone.length() < 7)
            return phone;
        return phone.substring(0, 4) + "***" + phone.substring(phone.length() - 3);
    }

    /**
     * Masks card number for privacy (****1234)
     * 
     * @param cardNumber The card number to mask
     * @return Masked card number
     */
    public static String maskCardNumber(String cardNumber) {
        if (ObjectUtils.isBlank(cardNumber) || cardNumber.length() < 4)
            return "****";
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }


    /**
     * Left pads string with character
     * 
     * @param str     The string to pad
     * @param length  Target length
     * @param padChar Padding character
     * @return Padded string
     */
    public static String leftPad(String str, int length, char padChar) {
        if (str == null)
            str = "";
        if (str.length() >= length)
            return str;
        StringBuilder sb = new StringBuilder();
        for (int i = str.length(); i < length; i++) {
            sb.append(padChar);
        }
        return sb.append(str).toString();
    }

    /**
     * Right pads string with character
     * 
     * @param str     The string to pad
     * @param length  Target length
     * @param padChar Padding character
     * @return Padded string
     */
    public static String rightPad(String str, int length, char padChar) {
        if (str == null)
            str = "";
        if (str.length() >= length)
            return str;
        StringBuilder sb = new StringBuilder(str);
        for (int i = str.length(); i < length; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }
}
