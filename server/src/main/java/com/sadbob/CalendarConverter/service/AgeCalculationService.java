package com.sadbob.CalendarConverter.service;


import com.sadbob.CalendarConverter.dto.responseDTO.age.AgeResponse;
import com.sadbob.CalendarConverter.exception.ConversionException;
import com.sadbob.CalendarConverter.exception.ValidationException;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;


@Service
public class AgeCalculationService {

    private static final Logger log = LoggerFactory.getLogger(AgeCalculationService.class);


    private final EthiopianDateConverter ethiopianConverter;
    private final HijriDateConverter hijriConverter;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AgeCalculationService(EthiopianDateConverter ethiopianConverter, HijriDateConverter hijriConverter) {
        this.ethiopianConverter = ethiopianConverter;
        this.hijriConverter = hijriConverter;
    }


    public AgeResponse calculateAge(String calendarType, String birthDate) {
        try {
            String[] parts = birthDate.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            LocalDate gregorianBirthDate = switch (CalendarType.fromCode(calendarType)) {
                case GREGORIAN -> LocalDate.of(year, month, day);
                case ETHIOPIAN -> ethiopianConverter.toGregorian(year, month, day);
                case HIJRI -> hijriConverter.toGregorian(year, month, day);
            };

            LocalDate today = LocalDate.now();

            // Validate birthdate is not in future
            if (gregorianBirthDate.isAfter(today)) {
                throw new ValidationException("Birth date cannot be in the future");
            }

            Period age = Period.between(gregorianBirthDate, today);
            LocalDate nextBirthday = gregorianBirthDate.plusYears(age.getYears() + 1);

            return new AgeResponse(
                    age.getYears(),
                    birthDate,
                    nextBirthday.format(dateFormatter),
                    "Age calculated successfully"
            );

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error calculating age for birth date: {} from calendar: {}", birthDate, calendarType, e);
            throw new ConversionException("Failed to calculate age: " + e.getMessage(), e);
        }
    }
}