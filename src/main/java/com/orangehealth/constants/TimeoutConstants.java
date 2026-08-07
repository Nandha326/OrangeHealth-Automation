package com.orangehealth.constants;

public final class TimeoutConstants {

    private TimeoutConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final int IMPLICIT_WAIT = 0;

    public static final int EXPLICIT_WAIT = 20;

    public static final int PAGE_LOAD_TIMEOUT = 30;

    public static final int SCRIPT_TIMEOUT = 30;
}