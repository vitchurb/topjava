package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * GKislin
 * 07.01.2015.
 */
public class DateTimeUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static <T extends Comparable<? super T>> boolean isBetween(T lt, T startValue, T endValue) {
        return (startValue == null || lt.compareTo(startValue) >= 0) &&
                (endValue == null || lt.compareTo(endValue) <= 0);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, dateFormatter);
    }

    public static LocalTime parseTime(String timeStr) {
        return LocalTime.parse(timeStr, timeFormatter);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
