package com.orangehealth.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * DateUtils
 *
 * Utility class for thread-safe date and time operations
 * using the Java Time API.
 */
public final class DateUtils {

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final DateTimeFormatter DEFAULT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Current LocalDate.
     */
    public static LocalDate currentDate() {

        return LocalDate.now();

    }

    /**
     * Current LocalDateTime.
     */
    public static LocalDateTime currentDateTime() {

        return LocalDateTime.now();

    }

    /**
     * Current UTC Instant.
     */
    public static Instant currentInstant() {

        return Instant.now();

    }

    /**
     * Format LocalDate.
     */
    public static String format(LocalDate date,
                                String pattern) {

        return date.format(
                DateTimeFormatter.ofPattern(pattern));

    }

    /**
     * Format LocalDateTime.
     */
    public static String format(LocalDateTime dateTime,
                                String pattern) {

        return dateTime.format(
                DateTimeFormatter.ofPattern(pattern));

    }

    /**
     * Default date format.
     */
    public static String format(LocalDate date) {

        return date.format(DEFAULT_DATE_FORMAT);

    }

    /**
     * Default datetime format.
     */
    public static String format(LocalDateTime dateTime) {

        return dateTime.format(DEFAULT_DATE_TIME_FORMAT);

    }

    /**
     * Parse LocalDate.
     */
    public static LocalDate parseDate(String value,
                                      String pattern) {

        return LocalDate.parse(
                value,
                DateTimeFormatter.ofPattern(pattern));

    }

    /**
     * Parse LocalDateTime.
     */
    public static LocalDateTime parseDateTime(
            String value,
            String pattern) {

        return LocalDateTime.parse(
                value,
                DateTimeFormatter.ofPattern(pattern));

    }

    /**
     * Future date.
     */
    public static LocalDate futureDate(long days) {

        return LocalDate.now().plusDays(days);

    }

    /**
     * Past date.
     */
    public static LocalDate pastDate(long days) {

        return LocalDate.now().minusDays(days);

    }

    /**
     * Future DateTime.
     */
    public static LocalDateTime futureDateTime(
            long hours) {

        return LocalDateTime.now().plusHours(hours);

    }

    /**
     * Past DateTime.
     */
    public static LocalDateTime pastDateTime(
            long hours) {

        return LocalDateTime.now().minusHours(hours);

    }

    /**
     * Days between dates.
     */
    public static long daysBetween(
            LocalDate start,
            LocalDate end) {

        return ChronoUnit.DAYS.between(start, end);

    }

    /**
     * Current Zoned DateTime.
     */
    public static ZonedDateTime now(ZoneId zoneId) {

        return ZonedDateTime.now(zoneId);

    }

    /**
     * Convert Zone.
     */
    public static ZonedDateTime convertZone(
            ZonedDateTime source,
            ZoneId targetZone) {

        return source.withZoneSameInstant(targetZone);

    }

}