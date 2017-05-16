package ru.javawebinar.topjava.annotation;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by vit on 15.05.2017.
 */
@Component
public class CustomDateTimeAnnotationFormatterFactory
        implements AnnotationFormatterFactory<CustomDateTime> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> fieldTypes = new HashSet<Class<?>>(1, 1);
        fieldTypes.add(LocalDateTime.class);
        return fieldTypes;
    }

    @Override
    public Printer<?> getPrinter(CustomDateTime customDateTime, Class<?> aClass) {
        return new CustomDateTimeFormatter(customDateTime.value());
    }

    @Override
    public Parser<?> getParser(CustomDateTime customDateTime, Class<?> aClass) {
        return new CustomDateTimeFormatter(customDateTime.value());
    }


    private static class CustomDateTimeFormatter implements Formatter<LocalDateTime> {
        private DateTimeFormatter formatter;

        public CustomDateTimeFormatter(String mask) {
            this.formatter = DateTimeFormatter.ofPattern(mask);
        }

        @Override
        public String print(LocalDateTime object, Locale locale) {
            try {
                return formatter.format(object);
            } catch (DateTimeException e) {
                throw new IllegalArgumentException("Print failed " + object, e);
            }
        }

        @Override
        public LocalDateTime parse(String text, Locale locale) throws DateTimeException {
            return LocalDateTime.parse(text, formatter);
        }
    }


}
