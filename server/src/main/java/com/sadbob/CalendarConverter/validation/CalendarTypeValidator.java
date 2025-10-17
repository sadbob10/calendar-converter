package com.sadbob.CalendarConverter.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CalendarTypeValidator implements ConstraintValidator<ValidCalendarType, String> {

    private static final List<String> VALID_CALENDAR_TYPES =
            Arrays.asList("gregorian", "ethiopian", "hijri", "greg", "eth", "hij");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_CALENDAR_TYPES.contains(value.toLowerCase());
    }
}