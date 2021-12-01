package ru.javawebinar.topjava.web.converter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class DateTimeFormatter{
    public static class FormatterLocalDate implements Formatter<LocalDate>{

        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
            return parseLocalDate(text);
        }

        @Override
        public String print(LocalDate object, Locale locale) {
            return object.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static class FormatterLocalTime implements Formatter<LocalTime>{

        @Override
        public LocalTime parse(String text, Locale locale) throws ParseException {
            return parseLocalTime(text);
        }

        @Override
        public String print(LocalTime object, Locale locale) {
            return object.format(java.time.format.DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }

}
