package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Temporal> boolean isBetweenHalfOpen(T ldt, T start, T end) {
        if (ldt instanceof LocalTime) {
            return ((LocalTime) ldt).compareTo((LocalTime) start) >= 0
                    && ((LocalTime) ldt).compareTo((LocalTime) end) < 0;
        }
        if (ldt instanceof LocalDateTime) {
            return ((LocalDateTime) ldt).compareTo((ChronoLocalDateTime<?>) start) >= 0
                    && ((LocalDateTime) ldt).compareTo((ChronoLocalDateTime<?>) end) <= 0;
        }
        return false;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

