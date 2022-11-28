package ru.practicum.explorewithme.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime toLocalDateTime(String stringTime) {
        return LocalDateTime.parse(stringTime, formatter);
    }

    public static String toString(LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }
}
