package com.orangehealth.utils;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import net.datafaker.Faker;

/**
 * RandomDataGenerator
 *
 * Utility class for generating test data.
 *
 * Thread-safe:
 * Uses ThreadLocal<Faker> and ThreadLocalRandom.
 */
public final class RandomDataGenerator {

    private static final ThreadLocal<Faker> FAKER =
            ThreadLocal.withInitial(() ->
                    new Faker(Locale.ENGLISH));

    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String ALPHA_NUMERIC =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private RandomDataGenerator() {
        throw new IllegalStateException("Utility class");
    }

    private static Faker faker() {
        return FAKER.get();
    }

    /**
     * Random first name.
     */
    public static String firstName() {

        return faker().name().firstName();

    }

    /**
     * Random last name.
     */
    public static String lastName() {

        return faker().name().lastName();

    }

    /**
     * Full name.
     */
    public static String fullName() {

        return faker().name().fullName();

    }

    /**
     * Random email.
     */
    public static String email() {

        return faker().internet().emailAddress();

    }

    /**
     * Random phone number.
     */
    public static String phoneNumber() {

        return faker().phoneNumber().cellPhone();

    }

    /**
     * Random numeric string.
     */
    public static String numeric(int length) {

        StringBuilder builder =
                new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            builder.append(
                    ThreadLocalRandom.current()
                            .nextInt(10));

        }

        return builder.toString();

    }

    /**
     * Random alphabetic string.
     */
    public static String alphabetic(int length) {

        StringBuilder builder =
                new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            builder.append(
                    ALPHABET.charAt(
                            ThreadLocalRandom.current()
                                    .nextInt(ALPHABET.length())));

        }

        return builder.toString();

    }

    /**
     * Random alphanumeric string.
     */
    public static String alphaNumeric(int length) {

        StringBuilder builder =
                new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            builder.append(
                    ALPHA_NUMERIC.charAt(
                            ThreadLocalRandom.current()
                                    .nextInt(ALPHA_NUMERIC.length())));

        }

        return builder.toString();

    }

    /**
     * Random UUID.
     */
    public static String uuid() {

        return UUID.randomUUID().toString();

    }

    /**
     * Random Patient ID.
     */
    public static String patientId() {

        return "PAT-"
                + numeric(8);

    }

    /**
     * Random age.
     */
    public static int age(int min,
                          int max) {

        return ThreadLocalRandom.current()
                .nextInt(min, max + 1);

    }

    /**
     * Random boolean.
     */
    public static boolean randomBoolean() {

        return ThreadLocalRandom.current()
                .nextBoolean();

    }

    /**
     * Random integer.
     */
    public static int randomInt(int min,
                                int max) {

        return ThreadLocalRandom.current()
                .nextInt(min, max + 1);

    }

}