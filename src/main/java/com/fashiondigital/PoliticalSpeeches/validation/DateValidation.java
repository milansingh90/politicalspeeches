package com.fashiondigital.PoliticalSpeeches.validation;

import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidation implements DateValidator {
//    private final DateTimeFormatter dateFormatter;
//
//    public DateValidation(DateTimeFormatter dateFormatter) {
//        this.dateFormatter = dateFormatter;
//    }

    @Override
    public boolean isValid(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
