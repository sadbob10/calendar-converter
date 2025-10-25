package com.sadbob.CalendarConverter.service.impl;

import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.enums.HolidayType;
import com.sadbob.CalendarConverter.repository.HolidayRepository;
import com.sadbob.CalendarConverter.service.interf.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidayServiceImpl implements HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayServiceImpl.class);

    private final HolidayRepository holidayRepository;

    public HolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
        initializeSampleHolidays();
    }

    @Override
    public List<Holiday> getHolidaysForDate(String calendarType, String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            CalendarType calType = CalendarType.fromCode(calendarType);
            return holidayRepository.findByCalendarTypeAndMonthNumberAndDayOfMonth(
                    calType, localDate.getMonthValue(), localDate.getDayOfMonth()
            );
        } catch (Exception e) {
            logger.error("Error fetching holidays for date {} of calendar type {}", date, calendarType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Holiday> getHolidaysForMonth(String calendarType, int year, int month) {
        try {
            CalendarType calType = CalendarType.fromCode(calendarType);
            return holidayRepository.findHolidaysForMonth(calType, month, year);
        } catch (Exception e) {
            logger.error("Error fetching holidays for month {}-{} of calendar type {}", year, month, calendarType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Holiday> getUpcomingHolidays(String calendarType, int currentMonth, int currentDay) {
        try {
            CalendarType calType = CalendarType.fromCode(calendarType);
            return holidayRepository.findUpcomingHolidays(calType, currentMonth, currentDay);
        } catch (Exception e) {
            logger.error("Error fetching upcoming holidays after {}-{} of calendar type {}", currentMonth, currentDay, calendarType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Holiday> getHolidaysByType(String calendarType, String holidayType) {
        try {
            CalendarType calType = CalendarType.fromCode(calendarType);
            HolidayType hType = HolidayType.valueOf(holidayType.toUpperCase());
            return holidayRepository.findByCalendarTypeAndHolidayType(calType, hType);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid holiday type: {}", holidayType, e);
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error fetching holidays of type {} for calendar type {}", holidayType, calendarType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getHolidayNamesForDate(String calendarType, String date) {
        return getHolidaysForDate(calendarType, date)
                .stream()
                .map(Holiday::getName)
                .collect(Collectors.toList());
    }

    private void initializeSampleHolidays() {
        if (holidayRepository.count() > 0) return;

        List<Holiday> sampleHolidays = new ArrayList<>();

        // Ethiopian Holidays
        sampleHolidays.add(new Holiday("Enkutatash", "Ethiopian New Year", CalendarType.ETHIOPIAN, HolidayType.NATIONAL, 1, 1, "ET"));
        sampleHolidays.add(new Holiday("Meskel", "Finding of the True Cross", CalendarType.ETHIOPIAN, HolidayType.RELIGIOUS, 17, 1, "ET"));
        sampleHolidays.add(new Holiday("Christmas", "Ethiopian Christmas - Genna", CalendarType.ETHIOPIAN, HolidayType.RELIGIOUS, 7, 4, "ET"));
        sampleHolidays.add(new Holiday("Timkat", "Epiphany", CalendarType.ETHIOPIAN, HolidayType.RELIGIOUS, 11, 5, "ET"));
        sampleHolidays.add(new Holiday("Adwa Victory", "Victory of Adwa", CalendarType.ETHIOPIAN, HolidayType.NATIONAL, 2, 6, "ET"));
        sampleHolidays.add(new Holiday("Easter", "Ethiopian Easter - Fasika", CalendarType.ETHIOPIAN, HolidayType.RELIGIOUS, 15, 7, null));

        // Islamic Holidays
        sampleHolidays.add(new Holiday("Ramadan", "Month of Fasting", CalendarType.HIJRI, HolidayType.RELIGIOUS, 1, 9, null));
        sampleHolidays.add(new Holiday("Eid al-Fitr", "End of Ramadan", CalendarType.HIJRI, HolidayType.RELIGIOUS, 1, 10, null));
        sampleHolidays.add(new Holiday("Eid al-Adha", "Feast of Sacrifice", CalendarType.HIJRI, HolidayType.RELIGIOUS, 10, 12, null));
        sampleHolidays.add(new Holiday("Islamic New Year", "Hijri New Year", CalendarType.HIJRI, HolidayType.RELIGIOUS, 1, 1, null));
        sampleHolidays.add(new Holiday("Mawlid", "Prophet's Birthday", CalendarType.HIJRI, HolidayType.RELIGIOUS, 12, 3, null));

        // Gregorian Holidays
        sampleHolidays.add(new Holiday("New Year's Day", "International New Year", CalendarType.GREGORIAN, HolidayType.INTERNATIONAL, 1, 1, null));
        sampleHolidays.add(new Holiday("Christmas", "Christmas Day", CalendarType.GREGORIAN, HolidayType.RELIGIOUS, 25, 12, null));
        sampleHolidays.add(new Holiday("Easter", "Easter Sunday", CalendarType.GREGORIAN, HolidayType.RELIGIOUS, 9, 4, null));

        holidayRepository.saveAll(sampleHolidays);
    }
}