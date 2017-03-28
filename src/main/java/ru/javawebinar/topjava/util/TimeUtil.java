package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * GKislin
 * 07.01.2015.
 */
public class TimeUtil {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : dateTimeFormatter.format(localDateTime);
    }

    public static String formatDate(LocalDateTime localDateTime) {
        return localDateTime == null ? null : dateFormatter.format(localDateTime);
    }

    public static String formatTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : timeFormatter.format(localDateTime);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, dateFormatter);
    }

    public static LocalTime parseTime(String timeStr) {
        return LocalTime.parse(timeStr, timeFormatter);
    }
}
