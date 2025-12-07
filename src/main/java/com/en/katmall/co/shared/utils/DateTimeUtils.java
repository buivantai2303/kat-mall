/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Utility class for date and time operations.
 * Provides date formatting, parsing and calculations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class DateTimeUtils {

    /** Default timezone for Vietnam */
    public static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    /** Common date formats */
    public static final DateTimeFormatter DATE_VN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_VN = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter DATETIME_FULL = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter TIME_ONLY = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private DateTimeUtils() {
        // Private constructor to prevent instantiation
    }


    /**
     * Gets current instant
     * 
     * @return Current instant
     */
    public static Instant now() {
        return Instant.now();
    }

    /**
     * Gets current LocalDateTime in Vietnam timezone
     * 
     * @return Current LocalDateTime
     */
    public static LocalDateTime nowVn() {
        return LocalDateTime.now(VN_ZONE);
    }

    /**
     * Gets current LocalDate in Vietnam timezone
     * 
     * @return Current LocalDate
     */
    public static LocalDate todayVn() {
        return LocalDate.now(VN_ZONE);
    }

    /**
     * Gets timestamp string for file naming
     * 
     * @return Timestamp string (yyyyMMddHHmmss)
     */
    public static String nowTimestamp() {
        return nowVn().format(TIMESTAMP);
    }


    /**
     * Formats Instant to Vietnamese date format (dd/MM/yyyy)
     * 
     * @param instant The instant to format
     * @return Formatted date string
     */
    public static String formatDateVn(Instant instant) {
        if (instant == null)
            return "";
        return instant.atZone(VN_ZONE).format(DATE_VN);
    }

    /**
     * Formats Instant to Vietnamese datetime format (dd/MM/yyyy HH:mm)
     * 
     * @param instant The instant to format
     * @return Formatted datetime string
     */
    public static String formatDateTimeVn(Instant instant) {
        if (instant == null)
            return "";
        return instant.atZone(VN_ZONE).format(DATETIME_VN);
    }

    /**
     * Formats Instant to full datetime format
     * 
     * @param instant The instant to format
     * @return Formatted datetime string
     */
    public static String formatFull(Instant instant) {
        if (instant == null)
            return "";
        return instant.atZone(VN_ZONE).format(DATETIME_FULL);
    }

    /**
     * Formats Instant to ISO date format (yyyy-MM-dd)
     * 
     * @param instant The instant to format
     * @return Formatted date string
     */
    public static String formatIso(Instant instant) {
        if (instant == null)
            return "";
        return instant.atZone(VN_ZONE).format(DATE_ISO);
    }

    /**
     * Formats LocalDateTime with custom pattern
     * 
     * @param dateTime The datetime to format
     * @param pattern  The format pattern
     * @return Formatted string
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null)
            return "";
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * Parses Vietnamese date format to LocalDate
     * 
     * @param dateStr Date string (dd/MM/yyyy)
     * @return Optional containing LocalDate
     */
    public static Optional<LocalDate> parseDateVn(String dateStr) {
        if (ObjectUtils.isBlank(dateStr))
            return Optional.empty();
        try {
            return Optional.of(LocalDate.parse(dateStr.trim(), DATE_VN));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses ISO date format to LocalDate
     * 
     * @param dateStr Date string (yyyy-MM-dd)
     * @return Optional containing LocalDate
     */
    public static Optional<LocalDate> parseDateIso(String dateStr) {
        if (ObjectUtils.isBlank(dateStr))
            return Optional.empty();
        try {
            return Optional.of(LocalDate.parse(dateStr.trim(), DATE_ISO));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses Vietnamese datetime format
     * 
     * @param dateTimeStr DateTime string (dd/MM/yyyy HH:mm)
     * @return Optional containing LocalDateTime
     */
    public static Optional<LocalDateTime> parseDateTimeVn(String dateTimeStr) {
        if (ObjectUtils.isBlank(dateTimeStr))
            return Optional.empty();
        try {
            return Optional.of(LocalDateTime.parse(dateTimeStr.trim(), DATETIME_VN));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }


    /**
     * Converts LocalDateTime to Instant using VN timezone
     * 
     * @param dateTime The LocalDateTime to convert
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime dateTime) {
        if (dateTime == null)
            return null;
        return dateTime.atZone(VN_ZONE).toInstant();
    }

    /**
     * Converts Instant to LocalDateTime using VN timezone
     * 
     * @param instant The Instant to convert
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null)
            return null;
        return instant.atZone(VN_ZONE).toLocalDateTime();
    }

    /**
     * Converts LocalDate to start of day Instant
     * 
     * @param date The date
     * @return Instant at start of day
     */
    public static Instant toStartOfDay(LocalDate date) {
        if (date == null)
            return null;
        return date.atStartOfDay(VN_ZONE).toInstant();
    }

    /**
     * Converts LocalDate to end of day Instant
     * 
     * @param date The date
     * @return Instant at end of day (23:59:59.999)
     */
    public static Instant toEndOfDay(LocalDate date) {
        if (date == null)
            return null;
        return date.atTime(LocalTime.MAX).atZone(VN_ZONE).toInstant();
    }


    /**
     * Gets time ago description (relative time)
     * 
     * @param instant The past instant
     * @return Human readable time ago string
     */
    public static String timeAgo(Instant instant) {
        if (instant == null)
            return "";

        long seconds = ChronoUnit.SECONDS.between(instant, Instant.now());

        if (seconds < 60)
            return "vừa xong";
        if (seconds < 3600)
            return (seconds / 60) + " phút trước";
        if (seconds < 86400)
            return (seconds / 3600) + " giờ trước";
        if (seconds < 604800)
            return (seconds / 86400) + " ngày trước";
        if (seconds < 2592000)
            return (seconds / 604800) + " tuần trước";
        if (seconds < 31536000)
            return (seconds / 2592000) + " tháng trước";
        return (seconds / 31536000) + " năm trước";
    }

    /**
     * Calculates days between two dates
     * 
     * @param start Start date
     * @param end   End date
     * @return Number of days
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null)
            return 0;
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Adds days to an instant
     * 
     * @param instant The base instant
     * @param days    Days to add
     * @return New instant
     */
    public static Instant plusDays(Instant instant, int days) {
        if (instant == null)
            return null;
        return instant.plus(days, ChronoUnit.DAYS);
    }

    /**
     * Adds hours to an instant
     * 
     * @param instant The base instant
     * @param hours   Hours to add
     * @return New instant
     */
    public static Instant plusHours(Instant instant, int hours) {
        if (instant == null)
            return null;
        return instant.plus(hours, ChronoUnit.HOURS);
    }


    /**
     * Checks if instant is in the past
     * 
     * @param instant The instant to check
     * @return true if past
     */
    public static boolean isPast(Instant instant) {
        if (instant == null)
            return false;
        return instant.isBefore(Instant.now());
    }

    /**
     * Checks if instant is in the future
     * 
     * @param instant The instant to check
     * @return true if future
     */
    public static boolean isFuture(Instant instant) {
        if (instant == null)
            return false;
        return instant.isAfter(Instant.now());
    }

    /**
     * Checks if date is today (VN timezone)
     * 
     * @param date The date to check
     * @return true if today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null)
            return false;
        return date.equals(todayVn());
    }

    /**
     * Checks if instant is between two other instants (inclusive)
     * 
     * @param instant The instant to check
     * @param start   Start of range
     * @param end     End of range
     * @return true if in range
     */
    public static boolean isBetween(Instant instant, Instant start, Instant end) {
        if (instant == null || start == null || end == null)
            return false;
        return !instant.isBefore(start) && !instant.isAfter(end);
    }
}
