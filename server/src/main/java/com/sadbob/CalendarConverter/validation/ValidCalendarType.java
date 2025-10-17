package com.sadbob.CalendarConverter.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CalendarTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCalendarType {
    String message() default "Invalid calendar type. Must be: gregorian, ethiopian, or hijri";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
